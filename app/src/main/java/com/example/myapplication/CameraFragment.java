package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.CameraFragmentBinding;
import com.example.myapplication.databinding.QuestionBoxBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment {
    private CameraFragmentBinding binding;
    private QuestionBoxAdapter QuestionBoxAdapter;
    private List<UserAccount> userAccounts = new ArrayList<>();
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CameraFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.reportView.setLayoutManager(new LinearLayoutManager(requireContext()));
        QuestionBoxAdapter = new QuestionBoxAdapter(new ArrayList<>());
        binding.reportView.setAdapter(QuestionBoxAdapter);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(uid, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();



        db.collection("Questions")
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserAccount userAccount = document.toObject(UserAccount.class);
                            if (userAccount != null) {
                                userAccounts.add(userAccount);
                            }
                        }
                        Log.d("FirestoreDebug", "Fetched data size: " + userAccounts.size());
                        sharedViewModel.setUserAccounts(userAccounts);
                    } else {
                        Log.e("FirestoreError", "Error fetching data", task.getException());
                    }
                });


        sharedViewModel.getUserAccounts().observe(getViewLifecycleOwner(), userAccounts -> {
            if (userAccounts != null) {
                QuestionBoxAdapter.updateUserAccounts(userAccounts);
                Log.d("LiveDataUpdate", "Data size in LiveData: " + userAccounts.size());
            } else {
                Log.d("LiveDataUpdate", "No data available");
            }
        });

        QuestionBoxAdapter.setOnItemClickListener(data -> {
            sendDataToServer(data);

            Fragment nextFragment = new CameraFragment2();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
            );
            transaction.replace(R.id.fragment_container, nextFragment);
            transaction.commit();
        });

        binding.reportView.setAdapter(QuestionBoxAdapter);

    }

    private void sendDataToServer(UserAccount data) {
        new Thread(() -> {
            try {
                String encodedPdfUri = URLEncoder.encode(data.getPdfUri(), "UTF-8");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", data.getEmail());
                jsonObject.put("id", data.getId());
                jsonObject.put("name", data.getEmail());
                jsonObject.put("pdfUrl", encodedPdfUri);
                jsonObject.put("question1", data.getQuestion1());
                jsonObject.put("question2", data.getQuestion2());
                jsonObject.put("question3", data.getQuestion3());
                jsonObject.put("question4", data.getQuestion4());
                jsonObject.put("videoPath", null);
                jsonObject.put("timestamp", data.getCurrentDate());

                URL url = new URL("http://34.64.206.14:8000/process");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                Log.d("JSON Payload", jsonObject.toString());
                int responseCode = connection.getResponseCode();

                Log.d("ServerResponse", "Response Code: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

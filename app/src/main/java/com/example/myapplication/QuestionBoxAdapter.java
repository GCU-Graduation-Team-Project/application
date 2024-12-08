package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.QuestionBoxBinding;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuestionBoxAdapter extends RecyclerView.Adapter<QuestionBoxAdapter.ViewHolder> {
    private List<UserAccount> accountList = new ArrayList<>();

    public QuestionBoxAdapter(List<UserAccount> userAccounts) {
        this.accountList = userAccounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionBoxBinding binding = QuestionBoxBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAccount data = accountList.get(position);
        holder.binding.questionView.setText(data.getQuestion1() + " + " + data.getQuestion2());
        holder.binding.dateView.setText(data.getCurrentDate());
        holder.binding.timeView.setText(data.getCurrentTime());

        holder.binding.selectButton.setOnClickListener(v -> {
            sendDataToServer(data);
        });
    }



    @Override
    public int getItemCount() {
        Log.d("RecyclerViewAdapter", "Adapter updated with size: " + accountList.size());
        return accountList.size();
    }

    public void updateUserAccounts(List<UserAccount> newAccounts) {
        if (newAccounts != null) {
            this.accountList = newAccounts;
            Log.d("AdapterUpdate", "Adapter updated with size: " + accountList.size());
            notifyDataSetChanged();
        } else {
            Log.d("AdapterUpdate", "New accounts list is null");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        QuestionBoxBinding binding;

        public ViewHolder(@NonNull QuestionBoxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void sendDataToServer(UserAccount data) {
        new Thread(() -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("question1", data.getQuestion1());
                jsonObject.put("question2", data.getQuestion2());
                jsonObject.put("question3", data.getQuestion3());
                jsonObject.put("question4", data.getQuestion4());
                jsonObject.put("pdf_url", data.getPdfUri());

                URL url = new URL("https://yourserver.com/api/upload");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                Log.d("ServerResponse", "Response Code: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



}

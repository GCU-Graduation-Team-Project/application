package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.ReportBoxBinding;
import com.example.myapplication.databinding.QuestionBoxBinding;

import java.util.ArrayList;
import java.util.List;

public class ReportBoxAdapter extends RecyclerView.Adapter<ReportBoxAdapter.ViewHolder> {
    private List<UserAccount> accountList = new ArrayList<>();
    private ReportBoxAdapter.OnItemClickListener listener;

    public ReportBoxAdapter(List<UserAccount> userAccounts) {
        this.accountList = userAccounts;
    }

    @NonNull
    @Override
    public ReportBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionBoxBinding binding = QuestionBoxBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReportBoxAdapter.ViewHolder(binding);
    }

    public interface OnItemClickListener {
        void onItemClick(UserAccount data);
    }

    public void setOnItemClickListener(ReportBoxAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull ReportBoxAdapter.ViewHolder holder, int position) {
        UserAccount data = accountList.get(position);
        holder.binding.questionView.setText(data.getQuestion1() + " + " + data.getQuestion2());
        holder.binding.dateView.setText(data.getCurrentDate());
        holder.binding.timeView.setText(data.getCurrentTime());

        holder.binding.selectButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(data);
            }
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


}

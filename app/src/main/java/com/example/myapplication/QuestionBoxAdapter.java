package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.QuestionBoxBinding;

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

    public void addItem(UserAccount account) {
        accountList.add(account);
        notifyItemInserted(accountList.size() - 1); // 마지막 위치에 새 항목 추가
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAccount data = accountList.get(position);
        holder.binding.questionView.setText(data.getQuestion1() + " + " + data.getQuestion2());
    }

    public void addData(UserAccount data) {
        accountList.add(data);
        notifyItemInserted(accountList.size() - 1);
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

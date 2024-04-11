package com.example.universitymarket.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universitymarket.R;
import com.example.universitymarket.adapters.myPostAdapter;
import com.example.universitymarket.globals.actives.ActiveUser;
import com.example.universitymarket.objects.Post;
import com.example.universitymarket.objects.User;
import com.example.universitymarket.utilities.Callback;
import com.example.universitymarket.utilities.Data;
import com.example.universitymarket.utilities.Network;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.util.List;

public class myPostFragment extends Fragment implements myPostAdapter.OnItemClickListener {
    private View root;
    private RecyclerView recyclerView;
    private TaskCompletionSource<String> load;
    private myPostAdapter adapter;
    private FragmentManager fm;
    private final Bundle dashMessage = new Bundle();

    public myPostFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mypost, container, false);
        configure(root);
        return root;
    }

    @Override
    public void onItemClicked(Post post) {
        Network.setPost(requireActivity(), post, true, new Callback<Post>() {
            @Override
            public void onSuccess(Post result) {
                ActiveUser.post_ids.remove(String.valueOf(post.getId()));
                Network.setUser(requireActivity(), Data.activeUserToPOJO(), false, new Callback<User>() {
                    @Override
                    public void onSuccess(User result) { Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show(); }
                    @Override
                    public void onFailure(Exception error) { Log.e("setUser", error.getMessage()); }
                });
            }
            @Override
            public void onFailure(Exception error) { Toast.makeText(requireActivity(), "Try Again Later", Toast.LENGTH_SHORT).show(); }
        });
    }

    private void configure(View v) {
        recyclerView = v.findViewById(R.id.mypost_recyclerView);

        load = new TaskCompletionSource<>();
        loadPage(load.getTask());
        Network.getPosts(requireActivity(), ActiveUser.post_ids, new Callback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> result) {
                adapter = new myPostAdapter(requireContext(), result, myPostFragment.this);
                load.setResult("getMyPosts");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onFailure(Exception error) {
                Log.e("getMyPosts", error.getMessage());
                Toast.makeText(
                        getContext(),
                        error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                load.setResult("getMyPosts");
            }
        });
    }

    private void loadPage(Task<String> task) {
        dashMessage.putBoolean("isLoading", true);
        fm.setFragmentResult("setLoading", dashMessage);

        task.addOnCompleteListener(res -> {
            dashMessage.putBoolean("isLoading", false);
            fm.setFragmentResult("setLoading", dashMessage);
        });
    }
}
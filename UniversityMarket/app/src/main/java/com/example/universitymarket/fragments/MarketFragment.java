package com.example.universitymarket.fragments;

import android.os.Bundle;

//import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.universitymarket.R;
import com.example.universitymarket.adapters.PostGVAdapter;
import com.example.universitymarket.globals.Policy;
import com.example.universitymarket.objects.Post;
import com.example.universitymarket.utilities.Callback;
import com.example.universitymarket.utilities.Network;
import com.example.universitymarket.utilities.PostModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MarketFragment extends Fragment {

    private FragmentManager fm;
    private GridView postsGV;  // Declare GridView as a class member
    private SwipeRefreshLayout swipeRefreshLayout;

    public MarketFragment(FragmentManager fm) {
        this.fm = fm;
    }

    ArrayList<Post> postsArrayList = new ArrayList<>();
    ArrayList <PostModel> postModelArrayList = new ArrayList<>();
    LayoutInflater viewPostInflater;
    ViewGroup viewSinglePostContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        postsGV = view.findViewById(R.id.idGVposts); // Find the GridView in your layout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        // Create the adapter and set it to the GridView with current posts
        PostGVAdapter adapter1 = new PostGVAdapter(getActivity(), postModelArrayList);
        postsGV.setAdapter(adapter1);


        // Set an refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPosts();
            }
        });

        getAllPosts();//initial fetch of posts in DB
//        // Set maximum length for post name
//        int maxLength = 20; // Change this value as needed
//
//        for (PostModel postModel : postModelArrayList) {
//            String originalTitle = postModel.getPost_name();
//            if (originalTitle.length() > maxLength) {
//                // Truncate the post name if it's too long
//                String truncatedTitle = originalTitle.substring(0, maxLength) + "...";
//                postModel.setPost_name(truncatedTitle);
//            }
//        }


        postsGridViewlistener(postsGV);

        return view;
    }

    /////
    private void postsGridViewlistener(GridView postsGV){
        postsGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Post selectedPost = postsArrayList.get(position); // Assuming this matches your data

                Bundle popupArgs = new Bundle();
                popupArgs.putString("popupTitle", selectedPost.getItemTitle());
                popupArgs.putString("popupFragment", viewPostFragment.class.getName());
                popupArgs.putStringArray("popupFragArgs", new String[]{ selectedPost.getId() });
                getParentFragmentManager().setFragmentResult("createPopup", popupArgs);
            }
        });
    }

    private void getAllPosts(){
        Network.getPosts(1, new Callback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> result) {
                postsArrayList.clear();
                postModelArrayList.clear();

                postsArrayList.addAll(result);

                //put all post into post model form
                for(Post p : postsArrayList){
                    Log.d("current post:" , p.getItemTitle());
                    List<String> imageUrls = p.getImageUrls().isEmpty() ? Policy.invalid_image : p.getImageUrls();
                    postModelArrayList.add(new PostModel("$"+ p.getListPrice() + " - " + p.getItemTitle(), imageUrls.get(0)));
                    Log.d("added " + p.getItemTitle() , "success");
                }
                if(postsGV != null){
                    PostGVAdapter adapter = (PostGVAdapter) postsGV.getAdapter();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);//stop refreshing animation
                }
            }
            @Override
            public void onFailure(Exception error) {
                swipeRefreshLayout.setRefreshing(false);// Stop the refreshing animation
                Log.e("Error loading posts", error.getMessage());
            }
        });
    }
}
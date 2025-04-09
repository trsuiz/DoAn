package com.example.doan.PlayerHome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.doan.EditProfileActivity;
import com.example.doan.Login;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView usernameTextView; //Khang THEM

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button editProfileBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


   //KHANG THEM

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Tìm VideoView
        VideoView videoView = view.findViewById(R.id.videoBackground);

        // Đặt đường dẫn video từ thư mục res/raw
        Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.bgvideo);
        videoView.setVideoURI(videoUri);

        // Lặp lại video và tắt âm thanh
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
           // mp.setVolume(0, 0);
        });

        // Bắt đầu phát video
        videoView.start();

        usernameTextView = view.findViewById(R.id.usernameTextView); // Đảm bảo ID đúng
        editProfileBtn = view.findViewById(R.id.editProfileBtn);






        // ✅ Mở EditProfileActivity khi nhấn nút
        editProfileBtn.setOnClickListener(v -> EditProfileActivity.openEditProfile(getActivity()));


        // ✅ Lấy FullName từ SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String fullName = preferences.getString("USERNAME", "Guest"); // Mặc định là "Guest"

        usernameTextView.setText(fullName); // Hiển thị lên TextView


        mAuth = FirebaseAuth.getInstance();

        Button logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut(); // Đăng xuất khỏi Firebase

            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(getActivity(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = requireActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        // Lấy lại username (email) để hiển thị
        String username = preferences.getString("USERNAME", "Guest");
        usernameTextView.setText(username);
    }

// KHANG THEM


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    */


}
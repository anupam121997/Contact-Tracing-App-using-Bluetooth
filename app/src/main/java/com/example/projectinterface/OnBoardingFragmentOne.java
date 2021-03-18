package com.example.projectinterface;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoardingFragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoardingFragmentOne extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnBoardingFragmentOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnBoardingFragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static OnBoardingFragmentOne newInstance(String param1, String param2) {
        OnBoardingFragmentOne fragment = new OnBoardingFragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_on_boarding_one, container, false);
        imageView=view.findViewById(R.id.right_arrow);

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
            public void onSwipeTop() {
//                Toast.makeText(getActivity().getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
//                Toast.makeText(getActivity().getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
//                Toast.makeText(getActivity().getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment1,new OnBoardingFragmentSecond()).commit();
            }
            public void onSwipeBottom() {
//                Toast.makeText(getActivity().getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
            }

        });

       imageView.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment1,new OnBoardingFragmentSecond()).commit();
               return super.onTouch(v, event);
           }
       });

        return view;
    }
}
package id.ac.umn.mobile.myapplication;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DescribeActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] dots;

    private Button mNextBtn;
    private Button mFinishBtn;
    private Button mPrevBtn;
    private int mCurrPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsScroll);

        mFinishBtn = (Button) findViewById(R.id.finishBtn);
        mNextBtn = (Button) findViewById(R.id.nextBtn);
        mPrevBtn = (Button) findViewById(R.id.prevBtn);
        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrPage + 1);
            }
        });

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrPage - 1);
            }
        });
    }

    public  void addDotsIndicator(int position){
        dots = new TextView[5];
        mDotLayout.removeAllViews();
        for (int i = 0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrPage = i;


            if(i == 0){
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(false);
                mFinishBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.INVISIBLE);
                mFinishBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setText("Next");
                mPrevBtn.setText("");
                mFinishBtn.setText("");
            }
            else if(i == dots.length - 1){
                mNextBtn.setEnabled(false);
                mPrevBtn.setEnabled(true);
                mFinishBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
                mFinishBtn.setVisibility(View.VISIBLE);
                mFinishBtn.setText("Sign In");
                mNextBtn.setText("");


                Button finishButton = (Button) findViewById(R.id.finishBtn);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(DescribeActivity.this, SignInActivity.class));
                    }
                });

                mPrevBtn.setText("Prev");
            }
            else{
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mFinishBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.VISIBLE);
                mFinishBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mPrevBtn.setText("Prev");
                mFinishBtn.setText("");

            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}


package id.ac.umn.mobile.myapplication;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public  SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images={
            R.drawable.upload_icon,
            R.drawable.share_icon,
            R.drawable.money_icon,
            R.drawable.artist_icon,
            R.drawable.world_icon,
    };

    public String[] slide_headings = {
            "Upload",
            "Share",
            "Profit",
            "Become Artist",
            "Show the World"
    };

    public String[] slide_desc = {
            "Create and show your creation" + " to the world!",
            "Share with your client"+"\n or your friends!",
            "Get profit" + "\n from your creation",
            "Start your new career"+"\n to amuse the world ",
            "Show what have you done" + "\n and express it to your hearts"
    };

    @Override
    public int getCount(){
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_title);
        TextView slideDesc = (TextView) view.findViewById(R.id.slide_details);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDesc.setText(slide_desc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}

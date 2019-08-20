package com.example.picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.picasso.provider.PicassoProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class SampleGridViewAdapter extends BaseAdapter {
  private final Context context;
  private final List<String> urls = new ArrayList<>();
  private final List<String> ciphers = new ArrayList<>();

  SampleGridViewAdapter(Context context) {
    this.context = context;

    // Ensure we get a different ordering of images on each run.
//    Collections.addAll(urls, Data.URLS);
//    Collections.shuffle(urls);
//
//    // Triple up the list.
//    ArrayList<String> copy = new ArrayList<>(urls);
//    urls.addAll(copy);
//    urls.addAll(copy);

    urls.add("http://pim.ycw.com/chat/pic/201908/08/f9666642e222bad3004e988f0bbdceee");
    urls.add("http://pim.ycw.com/chat/pic/201908/08/d30446edfa5ccc7afcb490eacb787689  ");
//    urls.add("http://pim.ycw.com/chat/pic/201908/07/9634f7e24f5c7f3032562922547a7bbf");
//    urls.add("http://pim.ycw.com/chat/pic/201908/07/041cc2b01dbc3923caa28f8b4f1755a8");

    ciphers.add("2006592603092681");
    ciphers.add("1981898608435563");
//    ciphers.add("7104511375077615");
//    ciphers.add("7104511375077615");
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    SquaredImageView view = (SquaredImageView) convertView;
    if (view == null) {
      view = new SquaredImageView(context);
      view.setScaleType(CENTER_CROP);
    }

    // Get the image URL for the current position.
    String url = getItem(position);

    // Trigger the download of the URL asynchronously into the image view.
    PicassoProvider.get() //
        .load(url)
        .placeholder(R.drawable.placeholder) //
        .error(R.drawable.error) //
        .cipher(ciphers.get(position))
        .tag(context) //
        .into(view);

    return view;
  }

  @Override public int getCount() {
    return urls.size();
  }

  @Override public String getItem(int position) {
//    return "http://pim.ycw.com/common/pic/201907/30/c7849c339b11129acd996e2103b11989";
//    return "http://pim.ycw.com/chat/pic/201907/31/f28f8fbfe3bfc6a04ec254e53d6e97dd";
    return urls.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }
}

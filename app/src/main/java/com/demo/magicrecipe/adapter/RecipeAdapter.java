package com.demo.magicrecipe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.magicrecipe.R;
import com.demo.magicrecipe.model.Recip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recip> {

	private ArrayList<Recip> items;
	private LayoutInflater inflater;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private DisplayImageOptions options;

	public RecipeAdapter(Context context, ArrayList<Recip> items) {
		super(context, 0, items);
		this.items = items;
		inflater = LayoutInflater.from(context);

		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_stub)
//				.showImageForEmptyUri(R.drawable.ic_empty)
//				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
				.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		final Recip recipeitem = items.get(position);
		if (view == null) {
			view = inflater.inflate(R.layout.row_recipe,null);
			holder = new ViewHolder();
			holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			holder.recipeTitle = (TextView) view.findViewById(R.id.recipeTitle);
			holder.recipeDescription = (TextView) view
					.findViewById(R.id.recipeDescription);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (recipeitem != null) {
			holder.recipeTitle.setText(recipeitem.title);
			holder.recipeDescription.setText(recipeitem.ingredients);
			if (recipeitem.thumbnail != null) {
				ImageLoader.getInstance().displayImage(recipeitem.thumbnail, holder.thumbnail,options, animateFirstListener);
			}
		}
		return view;

	}

	static class ViewHolder {
		ImageView thumbnail;
		TextView recipeTitle;
		TextView recipeDescription;
	}
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}

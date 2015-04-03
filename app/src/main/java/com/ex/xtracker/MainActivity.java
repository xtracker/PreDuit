package com.ex.xtracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ScreenSlidePagerAdapter(this.getSupportFragmentManager()));


        toolbar.inflateMenu(R.menu.menu_main);
        this.setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "未分类";
                case 1:
                    return "机器学习";
                case 2:
                    return "C语言";
                case 3:
                    return "C++";

            }

            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return new PlaceholderFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Return true to display menu
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;

        private RecyclerView.LayoutManager mLayoutManager;

        // ViewPager mViewPager;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mainRecyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);


/*            PagerTabStrip mPagerTab;
            mPagerTab=(PagerTabStrip)rootView.findViewById(R.id.pagerTab);
            //设置Tab选中时的颜色
            mPagerTab.setTabIndicatorColor(getResources().getColor(R.color.article_title));
            //设置Tab是否显示下划线
            mPagerTab.setDrawFullUnderline(true);
            //设置Tab背景色
            mPagerTab.setBackgroundColor(getResources().getColor(R.color.abc_background_cache_hint_selector_material_dark));
            //设置Tab间的距离？我感觉是这样
            mPagerTab.setTextSpacing(50); */

            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

                Paint paint = new Paint();

                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                    paint.setColor(getResources().getColor(R.color.itemSeparator));
                }

                @Override
                public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDrawOver(c, parent, state);
                    for (int i = 0, size = parent.getChildCount(); i < size; i++) {
                        View child = parent.getChildAt(i);
                        c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), paint);
                    }
                }
            });

             String[] dataSet = new String[] {"寻秦记", "神雕侠侣", "寻秦记", "寻秦记", "神雕侠侣", "寻秦记", "神雕侠侣", "寻秦记", "寻秦记", "神雕侠侣"};
            //Object[] dataSet = new Object[10];
            //dataSet[0] = new String[2];
            mAdapter = new HeaderFooterRecylerViewAdapter(dataSet);
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }
    }

    public static class HeaderFooterRecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private String[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View mView;
            public ItemViewHolder(View v) {
                super(v);
                mView = v;
            }
        }

        public static class HeaderViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public HeaderViewHolder(View v)
            {
                super(v);
                mView = v;
            }
        }

        private Bitmap thumbnail;

        // Provide a suitable constructor (depends on the kind of dataset)
        public HeaderFooterRecylerViewAdapter(String[] myDataset) {
            mDataset = myDataset;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 16;

            thumbnail =ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/IMG_20150121_164747.jpg", options), 120, 120);
        }

        // Create new views (invoked by the layout manager)

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_HEADER;

            return TYPE_ITEM;
        }



        protected HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recylcerview_header_layout, parent, false);

            ViewPager viewPager = (ViewPager)v.findViewById(R.id.viewHeaderPager);
            viewPager.setAdapter(new SlideShowViewPagerAdapter(parent.getContext()));

            HeaderViewHolder vh = new HeaderViewHolder(v);
            LinearLayout indicatorContainer = (LinearLayout)v.findViewById(R.id.indicatorContainer);
            for (int i = 0; i < 3; ++i) {
                View indicator = new View(parent.getContext());

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(parent.getContext(), 10), dip2px(parent.getContext(), 2));
                layoutParams.setMargins(dip2px(parent.getContext(), 2), dip2px(parent.getContext(), 2), dip2px(parent.getContext(), 2), dip2px(parent.getContext(), 2));

                indicator.setLayoutParams(layoutParams);

                if (i == 0)
                    indicator.setBackgroundColor(parent.getResources().getColor(R.color.textHeader));
                else
                    indicator.setBackgroundColor(parent.getResources().getColor(R.color.text_radio_normal));

                indicatorContainer.addView(indicator);
            }
            return vh;
        }

        private static class SlideShowViewPagerAdapter extends PagerAdapter
        {
            private String[] paths = {
                    Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/IMG_20150121_164747.jpg",
                    Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/IMG_20150121_195127.jpg"
            };

            private Context mContext;

            ImageView[] imageViews = new ImageView[2];

            public SlideShowViewPagerAdapter(Context context)
            {
                super();
                mContext = context;
            }

            @Override
            public int getCount() {
                return 2;
            }


            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                (container).removeView(imageViews[position]);
            }



            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                position %= 2;
                if (imageViews[position] == null)
                {
                    imageViews[position] = new ImageView(mContext);
                    imageViews[position].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 16;
                        Bitmap originalBmp =  BitmapFactory.decodeFile(paths[position], options);

                        imageViews[position] .setScaleType(ImageView.ScaleType.FIT_XY);
                        imageViews[position] .setImageBitmap(originalBmp);
                    }
                    catch (Exception ex)
                    {
                        ex = null;
                    }
                   //  Bitmap thumbnail = decodeSampledBitmapFromResource(paths[position], reqWidth, reqHeight);

                }

                ((ViewPager)container).addView(imageViews[position] , 0);
                return imageViews[position] ;
            }
        }

        protected ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recylerview_layout, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            if (viewType == TYPE_ITEM)
                return onCreateItemViewHolder(parent, viewType);
            else
                return onCreateHeaderViewHolder(parent, viewType);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            if (holder instanceof ItemViewHolder) {

                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                ((TextView) itemViewHolder.mView.findViewById(R.id.itemName)).setText(mDataset[position]);

                ImageView imageView = (ImageView) itemViewHolder.mView.findViewById(R.id.imageThumbnail);

                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 128;

                Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/IMG_20150121_164747.jpg", options);

                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp, 200, 200));*/
                imageView.setImageBitmap(thumbnail);

            } else if (holder instanceof HeaderViewHolder) {

            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}

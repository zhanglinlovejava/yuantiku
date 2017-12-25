package com.lin.yuantiku.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglin on 2016/5/20.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private View mContentView;
    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected int mLayoutResId;
    protected OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseAdapter(Context context, int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    public BaseAdapter(Context context, List<T> data) {
        this(context, 0, data);
    }

    public BaseAdapter(Context context, View contentView, List<T> data) {
        this(context, 0, data);
        mContentView = contentView;
    }

    public BaseAdapter(Context context) {
        this(context, null);
    }


    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    public void setNewData(List<T> data) {
//        this.mData = data;
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * additional data;
     *
     * @param data
     */
    public void addData(List<T> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * 请除集合里面所有的数据
     */
    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = onCreateDefViewHolder(parent, viewType);
        initItemClickListener(baseViewHolder);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        convert((BaseViewHolder) holder, mData.get(holder.getLayoutPosition()));
//        onBindDefViewHolder((BaseViewHolder) holder, mData.get(holder.getLayoutPosition()));
        if (mData.get(position) != null) {
            beforeConvert((BaseViewHolder) holder, mData.get(position), position);
            convert((BaseViewHolder) holder, mData.get(position));
            onBindDefViewHolder((BaseViewHolder) holder, mData.get(position));
        }
    }

    /**
     * @see #convert(BaseViewHolder, Object) ()
     * @deprecated This method is deprecated
     * {@link #convert(BaseViewHolder, Object)} depending on your use case.
     */
    @Deprecated
    protected void onBindDefViewHolder(BaseViewHolder holder, T item) {
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContentView == null) {
            return new BaseViewHolder(mContext, getItemView(layoutResId, parent));
        }
        return new BaseViewHolder(mContext, mContentView);
    }

    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }


    private void initItemClickListener(final BaseViewHolder baseViewHolder) {
        if (onRecyclerViewItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 解决Bug http://stackoverflow.com/questions/29684154/recyclerview-viewholder-getlayoutposition-vs-getadapterposition
                    int postion = baseViewHolder.getLayoutPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onRecyclerViewItemClickListener.onItemClick(v, postion);
                    }
                }
            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    protected void beforeConvert(BaseViewHolder helper, T item, int position) {
    }

    protected abstract void convert(BaseViewHolder helper, T item);


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

}

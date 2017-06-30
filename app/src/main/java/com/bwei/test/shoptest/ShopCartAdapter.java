package com.bwei.test.shoptest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 * time:
 * author:付智焱
 */

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder>{

    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnResfreshListener mOnResfreshListener;

    private  View headerView;
    private Context context;
    private List<ShopCartBean.OrderDataBean.CartlistBean> list=new ArrayList<>();

    public ShopCartAdapter(Context context, List<ShopCartBean.OrderDataBean.CartlistBean> list) {
        this.context = context;
        this.list = list;
    }
    private void showDialog(final View view, final int position){
        //调用删除某个规格商品的接口
        if(mOnDeleteClickListener != null){
            mOnDeleteClickListener.onDeleteClick(view,position,list.get(position).getId());
        }
        list.remove(position);
        //重新排序，标记所有商品不同商铺第一个的商品位置
//        MainActivity.isSelectFirst(list);
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(context).inflate(R.layout.item_shopcart,parent,false);

        return new ShopCartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopCartAdapter.MyViewHolder holder,final int position) {
        if(position>0){
            if(list.get(position).getShopId()==list.get(position-1).getShopId()){
                holder.llShopCartHeader.setVisibility(View.GONE);
            }else{
                holder.llShopCartHeader.setVisibility(View.VISIBLE);
            }
        }else {
            holder.llShopCartHeader.setVisibility(View.VISIBLE);
        }
        holder.tvShopCartClothColor.setText("颜色：" + list.get(position).getColor());
        holder.tvShopCartClothSize.setText("尺寸：" + list.get(position).getSize());
        holder.tvShopCartClothName.setText(list.get(position).getProductName());
        holder.tvShopCartShopName.setText(list.get(position).getShopName());
        holder.tvShopCartClothPrice.setText("¥" + list.get(position).getPrice());
        holder.etShopCartClothNum.setText(list.get(position).getCount() + "");

        Glide.with(context).load(list.get(position).getDefaultPic()).into(holder.ivShopCartClothPic);

        if(mOnResfreshListener != null){
            boolean isSelect = false;
            for(int i = 0;i < list.size(); i++){
                if(!list.get(i).getIsSelect()){
                    isSelect = false;
                    break;
                }else{
                    isSelect = true;
                }
            }
            mOnResfreshListener.onResfresh(isSelect);
        }

        holder.ivShopCartClothMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getCount() > 1) {
                    int count = list.get(position).getCount() - 1;
                    if (mOnEditClickListener != null) {
                        mOnEditClickListener.onEditClick(position, list.get(position).getId(), count);
                    }
                    list.get(position).setCount(count);
                    notifyDataSetChanged();
                }
            }
        });

        holder.ivShopCartClothAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = list.get(position).getCount() + 1;
                if(mOnEditClickListener != null){
                    mOnEditClickListener.onEditClick(position,list.get(position).getId(),count);
                }
                list.get(position).setCount(count);
                notifyDataSetChanged();
            }
        });

        if(list.get(position).getIsSelect()){
            holder.ivShopCartClothSel.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_selected));
        }else {
            holder.ivShopCartClothSel.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_unselected));
        }

        if(list.get(position).getIsSelect()){
            holder.ivShopCartShopSel.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_selected));
        }else {
            holder.ivShopCartShopSel.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_unselected));
        }

        holder.ivShopCartClothDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v,position);
            }
        });

        holder.ivShopCartClothSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setSelect(!list.get(position).getIsSelect());
                //通过循环找出不同商铺的第一个商品的位置
                for(int i = 0;i < list.size(); i++){
                    if(list.get(i).getIsFirst() == 1) {
                        //遍历去找出同一家商铺的所有商品的勾选情况
                        for(int j = 0;j < list.size();j++){
                            //如果是同一家商铺的商品，并且其中一个商品是未选中，那么商铺的全选勾选取消
                            if(list.get(j).getShopId() == list.get(i).getShopId() && !list.get(j).getIsSelect()){
                                list.get(i).setShopSelect(false);
                                break;
                            }else{
                                //如果是同一家商铺的商品，并且所有商品是选中，那么商铺的选中全选勾选
                                list.get(i).setShopSelect(true);
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        holder.ivShopCartShopSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getIsFirst() == 1) {
                    list.get(position).setShopSelect(!list.get(position).getIsSelect());
                    for(int i = 0;i < list.size();i++){
                        if(list.get(i).getShopId() == list.get(position).getShopId()){
                            list.get(i).setSelect(list.get(position).getIsSelect());
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int count = (list == null ? 0 : list.size());
        if(headerView != null){
            count++;
        }
        return count;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivShopCartShopSel;
        private TextView tvShopCartShopName;
        private TextView tvShopCartClothName;
        private TextView tvShopCartClothPrice;
        private TextView etShopCartClothNum;
        private TextView tvShopCartClothColor;
        private TextView tvShopCartClothSize;
        private ImageView ivShopCartClothSel;
        private ImageView ivShopCartClothMinus;
        private ImageView ivShopCartClothAdd;
        private ImageView ivShopCartClothDelete;
        private ImageView ivShopCartClothPic;
        private LinearLayout llShopCartHeader;
        public MyViewHolder(View itemView) {
            super(itemView);
            llShopCartHeader = (LinearLayout) itemView.findViewById(R.id.ll_shopcart_header);
            ivShopCartShopSel = (ImageView) itemView.findViewById(R.id.iv_item_shopcart_shopselect);
            tvShopCartShopName = (TextView) itemView.findViewById(R.id.tv_item_shopcart_shopname);
            tvShopCartClothName = (TextView) itemView.findViewById(R.id.tv_item_shopcart_clothname);
            tvShopCartClothPrice = (TextView) itemView.findViewById(R.id.tv_item_shopcart_cloth_price);
            etShopCartClothNum = (TextView) itemView.findViewById(R.id.et_item_shopcart_cloth_num);
            tvShopCartClothColor = (TextView) itemView.findViewById(R.id.tv_item_shopcart_cloth_color);
            tvShopCartClothSize = (TextView) itemView.findViewById(R.id.tv_item_shopcart_cloth_size);
            ivShopCartClothSel = (ImageView) itemView.findViewById(R.id.tv_item_shopcart_clothselect);
            ivShopCartClothMinus = (ImageView) itemView.findViewById(R.id.iv_item_shopcart_cloth_minus);
            ivShopCartClothAdd = (ImageView) itemView.findViewById(R.id.iv_item_shopcart_cloth_add);
            ivShopCartClothPic = (ImageView) itemView.findViewById(R.id.iv_item_shopcart_cloth_pic);
            ivShopCartClothDelete = (ImageView) itemView.findViewById(R.id.iv_item_shopcart_cloth_delete);
        }

        public View getHeaderView() {
            return headerView;
        }



    }
    private ShopCartAdapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnDeleteClickListener{
        void onDeleteClick(View view, int position, int cartid);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener){
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnEditClickListener{
        void onEditClick(int position, int cartid, int count);
    }

    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener){
        this.mOnEditClickListener = mOnEditClickListener;
    }

    public interface OnResfreshListener{
        void onResfresh(boolean isSelect);
    }

    public void setResfreshListener(OnResfreshListener mOnResfreshListener){
        this.mOnResfreshListener = mOnResfreshListener;
    }
}

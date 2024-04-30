package com.easyder.club.module.enjoy.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.enjoy.vo.GridImageVo;
import com.easyder.club.module.enjoy.vo.UploadVo;

import java.util.ArrayList;
import java.util.List;


/**
 * Author：sky on 2019/12/4 17:12.
 * Email：xcode126@126.com
 * Desc：
 */

public class GridImageAdapter extends BaseQuickAdapter<GridImageVo, BaseRecyclerHolder> {

    private boolean isNeedDelete;
    private int max;//最多可添加几张，给只做显示没关系

    /**
     * 仅做显示，不可增删
     */
    public GridImageAdapter() {
        super(R.layout.item_grid_image);
        this.isNeedDelete = false;
    }

    /**
     * 可增删
     *
     * @param data
     * @param max
     */
    public GridImageAdapter(List<GridImageVo> data, int max) {
        super(R.layout.item_grid_image, data);
        this.isNeedDelete = true;
        this.max = max;
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, GridImageVo item) {
        if (item.isAdd) {
            helper.setImageResource(R.id.image, R.drawable.add_picture);
            helper.setVisible(R.id.iv_del, false);
        } else {
            helper.setImageManager(mContext, R.id.image, item.path, R.drawable.ic_placeholder_1,R.drawable.ic_placeholder_1);
            helper.setVisible(R.id.iv_del, isNeedDelete);
//            ImageView ivImage = helper.getView(R.id.image);
//            ivImage.setImageURI(Uri.parse(item.path));
        }
        helper.addOnClickListener(R.id.iv_del);
    }

    /**
     * 是否是最大限制数量
     *
     * @return
     */
    public boolean isMaxLimit() {
        if (mData != null && mData.size() <= max) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否允许添加
     *
     * @param position
     * @return
     */
    public boolean isCanAdd(int position) {
        if (mData != null && mData.size() > 0) {
            GridImageVo gridImageVo = mData.get(position);
            return gridImageVo.isAdd;
        }
        return false;
    }

    public int canAddMax() {
        if (mData != null && mData.size() > 0) {
            return max - mData.size() + 1;
        }
        return 0;
    }

    /**
     * 插入图片
     *
     * @param dataVo
     */
    public void insertPicture(UploadVo dataVo) {
        if (mData != null && mData.size() > 0) {
            mData.add(mData.size() - 1, new GridImageVo(false, dataVo.imgurl));
        }
        //数组大小超过（最大限制+1）移除最后一个（添加图片对象），不允许添加
        if (mData != null && mData.size() >= max + 1) {
            mData.remove(mData.size() - 1);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除图片
     *
     * @param position
     */
    public void deletePicture(int position) {
        if (mData != null && mData.size() > 0) {
            GridImageVo gridImageVo = mData.get(position);
            if (!gridImageVo.isAdd) {
                mData.remove(position);
            }
        }
        //每次移除，如果最后一个不是添加图片对象，则添加一个允许添加
        if (mData != null && mData.size() > 0) {
            GridImageVo gridImageVo = mData.get(mData.size() - 1);
            if (!gridImageVo.isAdd) {
                mData.add(new GridImageVo(true, ""));
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 清空，或初始化操作
     */
    public void initPicture() {
        if (mData != null) {
            mData.clear();
            mData.add(new GridImageVo(true, ""));
        }
        notifyDataSetChanged();
    }

    /**
     * 获取添加的图片
     *
     * @return
     */
    public List<GridImageVo> getSelectPic() {
        List<GridImageVo> list = new ArrayList<>();
        if (mData != null && mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                GridImageVo gridImageVo = mData.get(i);
                if (!gridImageVo.isAdd) {
                    list.add(gridImageVo);
                }
            }
        }
        return list;
    }

    /**
     * 获取添加的图片路径
     *
     * @return
     */
    public String getSelectPath() {
        StringBuilder sb = new StringBuilder();
        if (mData != null && mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                GridImageVo gridImageVo = mData.get(i);
                if (!gridImageVo.isAdd) {
                    sb.append(gridImageVo.path);
                    sb.append(";");
                }
            }
        }
        return sb.toString().trim();
    }

    public int getPictureCount() {
        if (mData != null && mData.size() > 1) {
            return mData.size() - 1;
        } else {
            return 0;
        }
    }

}

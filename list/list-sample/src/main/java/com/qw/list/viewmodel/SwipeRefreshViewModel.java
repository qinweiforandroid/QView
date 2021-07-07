package com.qw.list.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qw.list.repository.Callback;
import com.qw.list.repository.GankRepository;
import com.qw.list.repository.entities.Image;
import com.qw.widget.list.PullRecyclerView;

import java.util.ArrayList;

/**
 * Created by qinwei on 2020/7/12 11:24 AM
 * email: qinwei_it@163.com
 */
public class SwipeRefreshViewModel extends AndroidViewModel {
    private int pageNumber = 1;
    private int nextNumber;
    private MediatorLiveData<ArrayList<Image>> images = new MediatorLiveData<>();
    private MediatorLiveData<String> errMsg = new MediatorLiveData<>();
    private int currentMode;


    public SwipeRefreshViewModel(@NonNull Application application) {
        super(application);
    }

    public void onRefresh() {
        currentMode = PullRecyclerView.MODE_PULL_TO_START;
        nextNumber = 1;
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        GankRepository.loadGankMeizhiByPage(nextNumber, new Callback<ArrayList<Image>>() {
            @Override
            public void onSuccess(ArrayList<Image> data) {
                pageNumber = nextNumber;
                images.setValue(data);
            }

            @Override
            public void onFailure(int code, String msg) {
                errMsg.setValue(msg);
            }
        });
    }

    public void onLoadMore() {
        currentMode = PullRecyclerView.MODE_PULL_TO_END;
        nextNumber = pageNumber + 1;
        loadDataFromServer();
    }

    public boolean isLoadMore() {
        return currentMode == PullRecyclerView.MODE_PULL_TO_END;
    }

    public boolean isRefresh() {
        return currentMode == PullRecyclerView.MODE_PULL_TO_START;
    }

    public MediatorLiveData<ArrayList<Image>> getImages() {
        return images;
    }

    public MediatorLiveData<String> getErrMsg() {
        return errMsg;
    }

    public static class ExposureUpload {
        /**
         * 记录已上传的位置
         */
        private ArrayList<Integer> uploadDonePositions = new ArrayList<>();

        /**
         * 获取上传的曝光数据
         *
         * @param firstPosition 数据开始位置
         * @param lastPosition  数据结束位置
         * @return 上传的曝光数据位置集合
         */
        public ArrayList<Integer> getUploadPositions(int firstPosition, int lastPosition) {
            ArrayList<Integer> willUploadPositions = new ArrayList<>();
            for (int i = firstPosition; i <= lastPosition; i++) {
                if (!uploadDonePositions.contains(i)) {
                    willUploadPositions.add(i);
                }
            }
            return willUploadPositions;
        }

        public void markUploadDone(ArrayList<Integer> uploadDonePositions) {
            this.uploadDonePositions.addAll(uploadDonePositions);
        }
    }

    public static class ExposureTracker {
        private static ExposureTracker mInstance;
        private RecyclerView recyclerView;
        private OnExposureListener listener;

        public ExposureTracker(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            recyclerView.addOnScrollListener(onScrollListener);
        }

        public static ExposureTracker getInstance() {
            if (mInstance == null) {
                throw new IllegalArgumentException("you should init ExposureTracker");
            }
            return mInstance;
        }

        public static void init(RecyclerView recyclerView) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mInstance = new ExposureTracker(recyclerView);
            } else {
                throw new IllegalArgumentException("ExposureTracker only support LinearLayoutManager or subclass");
            }
        }

        public void setOnExposureListener(OnExposureListener listener) {
            this.listener = listener;
        }

        private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int first = layoutManager.findFirstVisibleItemPosition();
                    int last = layoutManager.findLastVisibleItemPosition();
                    if (listener != null) {
                        listener.onExposureChanged(first, last);
                    }
                }
            }
        };

        public void release() {
            if (mInstance != null) {
                recyclerView.removeOnScrollListener(onScrollListener);
                listener = null;
                recyclerView = null;
                mInstance = null;
            }
        }

        public interface OnExposureListener {
            /**
             * 曝光数据变动
             *
             * @param firstPosition 起始位置
             * @param lastPosition  截止位置
             */
            void onExposureChanged(int firstPosition, int lastPosition);
        }
    }
}
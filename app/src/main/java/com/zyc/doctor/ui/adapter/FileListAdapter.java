package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.zyc.doctor.R;
import com.zyc.doctor.data.OnEventTriggerListener;
import com.zyc.doctor.tools.FileTransferServer;

import java.io.File;
import java.util.List;

import com.zyc.doctor.http.data.FileBean;
import com.zyc.doctor.ui.base.adapter.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.adapter.BaseViewHolder;
import com.zyc.doctor.utils.DirHelper;
import com.zyc.doctor.widgets.view.CircleProgressBar;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class FileListAdapter extends BaseRecyclerAdapter<FileBean>
{
    private Context context;
    private OnEventTriggerListener onEventTriggerListener;

    public FileListAdapter(Context context, List<FileBean> list)
    {
        super(list);
        this.context = context;
    }

    public void setOnEventTriggerListener(OnEventTriggerListener onEventTriggerListener)
    {
        this.onEventTriggerListener = onEventTriggerListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_file, parent, false);
        return new ApplyCooperateHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, FileBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyCooperateHolder extends BaseViewHolder<FileBean>
    {
        private TextView tvFileName, tvStatus, tvDown;
        private CircleProgressBar circleProgressBar;

        public ApplyCooperateHolder(View itemView)
        {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.item_file_name);
            tvStatus = itemView.findViewById(R.id.item_file_status);
            tvDown = itemView.findViewById(R.id.item_file_down);
            circleProgressBar = itemView.findViewById(R.id.item_file_progress);
        }

        @Override
        public void showView(final int position, final FileBean item)
        {
            String fileName = item.getFileName();
            String filePath = DirHelper.getPathFile() + "/" + fileName;
            File file = new File(filePath);
            if (file != null && file.exists())
            {
                tvDown.setVisibility(View.GONE);
                tvStatus.setVisibility(View.VISIBLE);
            }
            else
            {
                tvDown.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.GONE);
            }
            tvFileName.setText(fileName);
            tvStatus.setOnClickListener(v ->
                                        {
                                            if (openFileListener != null)
                                            {
                                                openFileListener.onOpen(position,filePath,fileName);
                                            }
                                        });
            tvDown.setOnClickListener(v ->
                                      {
                                          FileTransferServer.getInstance(context)
                                                            .downloadFile(position,
                                                                          item.getFileUrl(),
                                                                          DirHelper.getPathFile(),
                                                                          fileName,
                                                                          new DownloadListener()
                                                                          {
                                                                              @Override
                                                                              public void onDownloadError(
                                                                                      int what,
                                                                                      Exception exception)
                                                                              {
                                                                                  tvDown.setClickable(
                                                                                          true);
                                                                                  tvDown.setText(
                                                                                          "重试");
                                                                              }

                                                                              @Override
                                                                              public void onStart(
                                                                                      int what,
                                                                                      boolean isResume,
                                                                                      long rangeSize,
                                                                                      Headers responseHeaders,
                                                                                      long allCount)
                                                                              {
                                                                                  circleProgressBar.setVisibility(
                                                                                          View.VISIBLE);
                                                                                  tvDown.setText(
                                                                                          "下载中");
                                                                                  tvDown.setClickable(
                                                                                          false);
                                                                              }

                                                                              @Override
                                                                              public void onProgress(
                                                                                      int what,
                                                                                      int progress,
                                                                                      long fileCount,
                                                                                      long speed)
                                                                              {
                                                                                  circleProgressBar.setProgress(
                                                                                          progress);
                                                                              }

                                                                              @Override
                                                                              public void onFinish(
                                                                                      int what,
                                                                                      String filePath)
                                                                              {
                                                                                  circleProgressBar.setVisibility(
                                                                                          View.GONE);
                                                                                  tvStatus.setVisibility(
                                                                                          View.VISIBLE);
                                                                                  tvDown.setVisibility(
                                                                                          View.GONE);
                                                                              }

                                                                              @Override
                                                                              public void onCancel(
                                                                                      int what)
                                                                              {
                                                                              }
                                                                          });
                                      });
        }
    }

    private OpenFileListener openFileListener;

    public void setOpenFileListener(OpenFileListener openFileListener)
    {
        this.openFileListener = openFileListener;
    }

    public interface OpenFileListener
    {
        void onOpen(int position,String path,String fileName);
    }
}

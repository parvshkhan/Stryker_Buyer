package com.quickblox.sample.chat.ui.adapters;

/**
 * Created by igorkhomenko on 9/12/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.stryker.R;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.users.model.QBUser;

import java.util.List;

// amr import vc908.stickerfactory.StickersManager;

public class DialogsAdapter extends BaseAdapter {
    private List<QBDialog> dataSource;
    private LayoutInflater inflater;

    public DialogsAdapter(List<QBDialog> dataSource, Context ctx) {
        this.dataSource = dataSource;
        if (ctx != null)
            this.inflater = LayoutInflater.from(ctx);
    }

    public List<QBDialog> getDataSource() {
        return dataSource;
    }


    public long getItemId(int position) {
        return position;
    }


    public Object getItem(int position) {
        return dataSource.get(position);
    }


    public int getCount() {
        return dataSource.size();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // initIfNeed view
        //
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_room, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.roomName);
            holder.lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);
            //  holder.groupType = (TextView)convertView.findViewById(R.id.textViewGroupType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        //
        QBDialog dialog = dataSource.get(position);
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            holder.name.setText(dialog.getName());
        } else {
            // get opponent name for private dialog
            //
            Integer opponentID = ChatService.getInstance().getOpponentIDForPrivateDialog(dialog);
            QBUser user = ChatService.getInstance().getDialogsUsers().get(opponentID);
            if (user != null) {
                holder.name.setText(user.getLogin() == null ? user.getFullName() : user.getLogin());
            }
        }
     /*   if (dialog.getLastMessage() != null && StickersManager.isSticker(dialog.getLastMessage())) {
            holder.lastMessage.setText("Sticker");
        } else { */
        holder.lastMessage.setText(dialog.getLastMessage());
        //amr  }

        //  holder.groupType.setText(dialog.getType().toString());

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView lastMessage;
        //  TextView groupType;
    }
}

package com.app.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jsoncall.JsonCall;
import com.app.stryker.ProductListActivity;
import com.app.stryker.R;
import com.app.utill.AppUtil;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.sample.chat.core.Chat;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.sample.chat.core.GroupChatImpl;
import com.quickblox.sample.chat.core.PrivateChatImpl;
import com.quickblox.sample.chat.ui.activities.BaseActivity;
import com.quickblox.sample.chat.ui.activities.ChatActivity;
import com.quickblox.sample.chat.ui.adapters.ChatAdapter;
import com.quickblox.sample.chat.ui.adapters.DialogsAdapter;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.ui.OnEmojiBackspaceClickListener;
import vc908.stickerfactory.ui.OnStickerSelectedListener;
import vc908.stickerfactory.ui.fragment.StickersFragment;
import vc908.stickerfactory.ui.view.KeyboardHandleRelativeLayout;*/
//import com.example.testchat.MainActivity;

public class ChatListFragment extends android.support.v4.app.Fragment /*implements
        KeyboardHandleRelativeLayout.KeyboardSizeChangeListener*/ {

    Context context;

    /***/
    public static final String EXTRA_DIALOG = "dialog";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    private EditText messageEditText;
    private ListView messagesContainer;
    private Button sendButton;
    private ProgressBar progressBar;
    private ChatAdapter adapter;

    private Chat chat;
    private QBDialog dialog;
    // amr private KeyboardHandleRelativeLayout keyboardHandleLayout;
    private View stickersFrame;
    private boolean isStickersFrameVisible;
    private ImageView stickerButton;
    private RelativeLayout container;
    ProductListActivity mactivity;
    ProgressDialog dialog1;
    String sellerId = "", dialogId = "";

    boolean flagLoadedChat = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootview = inflater.inflate(R.layout.fragment_chat_layout,
                container, false);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        // Get dialogs if the session is active

        try {

            initViews();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (AppUtil.isNetworkAvailable(context)) {
            ProductListActivity activity = (ProductListActivity) getActivity();
            final String mySellerId = activity.getSellerId();
            //dialog1 = ProgressDialog.show(ChatListFragment.this,"", "please wait..");
            GetDialogTask task = new GetDialogTask();
            task.execute(new String[]{mySellerId, AppUtil.getUserId(context)});
        } else {

        }

        try {

            //if (((BaseActivity) getActivity()).isSessionActive()) {
            //	getDialogs();
            //}

			/*
			 * ChatService.initIfNeed(getActivity()); final QBUser user = new
			 * QBUser(); user.setLogin(ApplicationSingletonBuyer.USER_LOGIN);
			 * user.setPassword(ApplicationSingletonBuyer.USER_PASSWORD);
			 * 
			 * 
			 * 
			 * ChatService.getInstance().login(user, new QBEntityCallbackImpl()
			 * {
			 * 
			 * @Override public void onSuccess() { // Go to Dialogs screen //
			 * Log.e("response", " in side success--->");
			 * 
			 * 
			 * if(((BaseActivity) getActivity()).isSessionActive()){
			 * getDialogs(); }
			 * 
			 * 
			 * // Amrit done
			 * 
			 * }
			 * 
			 * @Override public void onError(List errors) { Log.e("response",
			 * " in side failure--->"+errors.get(0).toString());
			 * if(errors.get(0).toString()=="You have already logged in chat") {
			 * 
			 * 
			 * } else
			 * if(errors.get(0).toString()=="You have already logged in chat") {
			 * getDialogs();
			 * 
			 * }
			 * 
			 * 
			 * // AlertDialog.Builder dialog = new
			 * AlertDialog.Builder(MainActivity.this); //
			 * dialog.setMessage("chat login errors: " +
			 * errors).create().show(); } });
			 */

            //

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getDialogs() {
        // progressBar.setVisibility(View.VISIBLE);

        ChatService.getInstance().getDialogs(new QBEntityCallbackImpl() {
            @Override
            public void onSuccess(Object object, Bundle bundle) {
                // progressBar.setVisibility(View.GONE);

                final ArrayList<QBDialog> dialogs = (ArrayList<QBDialog>) object;

                // build list view
                //
                if (dialogs != null)
                    buildListView(dialogs);
            }

            @Override
            public void onError(List errors) {
                // progressBar.setVisibility(View.GONE);
                Log.e("response", " AAAAAAAAAAAAAAAAAA--->"
                        + errors.get(0).toString());
				/*AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setMessage("get dialogs errors: " + errors).create()
						.show();*/
            }
        });
    }

    public void buildListView(List<QBDialog> dialogs) {
        //final DialogsAdapter adapter = new DialogsAdapter(dialogs,
        //		context);
        if (!dialogId.equalsIgnoreCase("")) {
            if (adapter != null && adapter.getCount() > 0) {
                //	QBDialog selectedDialog = (QBDialog) adapter.getItem(0);

                //Bundle bundle = new Bundle();
                //bundle.putSerializable(ChatActivity.EXTRA_DIALOG, selectedDialog);
                //dialog = selectedDialog;


                final DialogsAdapter adapter = new DialogsAdapter(dialogs, context);
                int i = 0;
                QBDialog selectedDialog = null;
                String tempdialogid = dialogId;

                while (adapter.getCount() > 0) {

                    adapter.getItem(i);
                    selectedDialog = (QBDialog) adapter.getItem(i);
                    Log.e("sss--------->", selectedDialog.getDialogId());
                    if (selectedDialog.getDialogId().equalsIgnoreCase(tempdialogid)) {


                        break;
                    } else {

                    }

                    i++;

                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(ChatActivity.EXTRA_DIALOG, selectedDialog);

                if (getView() != null) {    //initViews();

                    try {
                        if (((BaseActivity) getActivity()).isSessionActive()) {
                            initChat();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {

                    Log.e("getviewnull---->", "in  getview null");

                }
            } else

            {

			/*
			 * No dialog found hence do the following
			 * 
			 * 1 Create new dialog 2 save dialog id to database 3 call chat
			 * activity
			 */
                ProductListActivity activity = (ProductListActivity) getActivity();
                if (activity != null) {
                    final String mySellerId = activity.getSellerId();

                    final String mySellerChatId = activity.getSellerChatId();


                    if (!mySellerChatId.equalsIgnoreCase("")) {
                        Log.e("--------->", mySellerId);
                        QBPrivateChatManager privateChatManager = QBChatService
                                .getInstance().getPrivateChatManager();
                        privateChatManager.createDialog(Integer.parseInt(mySellerChatId),
                                new QBEntityCallbackImpl<QBDialog>() {
                                    @Override
                                    public void onSuccess(QBDialog dialog1, Bundle args) {

                                        Log.e("dialog create", "dialog created");
                                        dialog = dialog1;

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(ChatActivity.EXTRA_DIALOG,
                                                dialog);

                                        AddDialogTask task = new AddDialogTask();
                                        task.execute(new String[]{mySellerId,
                                                AppUtil.getUserId(context),
                                                dialog.getDialogId()});
                                        //initViews();

                                        try {
                                            if (((BaseActivity) getActivity()).isSessionActive()) {
                                                initChat();
                                            }
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        //amritChatActivity.start(getActivity(), bundle);
                                        if (adapter != null) {
                                            adapter.notifyDataSetChanged();
                                        }

                                    }

                                    @Override
                                    public void onError(List<String> errors) {
                                        Log.e("dialog failed---------->", "dialog failed" + errors);

                                    }
                                });
                    }
                }

            }

        } else {
		
		


	/*
	 * No dialog found hence do the following
	 * 
	 * 1 Create new dialog 2 save dialog id to database 3 call chat
	 * activity
	 */
            ProductListActivity activity = (ProductListActivity) getActivity();

            if (activity != null) {
                final String mySellerId = activity.getSellerId();
                final String mySellerChatId = activity.getSellerChatId();

                if (!mySellerChatId.equalsIgnoreCase("")) {
                    Log.e("--------->", mySellerId);
                    QBPrivateChatManager privateChatManager = QBChatService
                            .getInstance().getPrivateChatManager();
                    privateChatManager.createDialog(Integer.parseInt(mySellerChatId),
                            new QBEntityCallbackImpl<QBDialog>() {
                                @Override
                                public void onSuccess(QBDialog dialog1, Bundle args) {

                                    Log.e("dialog create", "dialog created");
                                    dialog = dialog1;

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(ChatActivity.EXTRA_DIALOG,
                                            dialog);

                                    AddDialogTask task = new AddDialogTask();
                                    task.execute(new String[]{mySellerId,
                                            AppUtil.getUserId(context),
                                            dialog.getDialogId()});
                                    //initViews();

                                    try {
                                        if (((BaseActivity) getActivity()).isSessionActive()) {
                                            initChat();
                                        }
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    //amritChatActivity.start(getActivity(), bundle);
                                    if (adapter != null) {
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onError(List<String> errors) {
                                    Log.e("dialog failed---------->", "dialog failed" + errors);

                                }
                            });


                }

            }


            ChatService.getInstance().addConnectionListener(chatConnectionListener);

        }

    }

    private void initViews() {
        messagesContainer = (ListView) getView().findViewById(
                R.id.messagesContainer);
        messageEditText = (EditText) getView().findViewById(R.id.messageEdit);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        TextView companionLabel = (TextView) getView().findViewById(
                R.id.companionLabel);
        container = (RelativeLayout) getView().findViewById(R.id.container);

        ProductListActivity activity = (ProductListActivity) getActivity();
        // Setup opponents info
        //
        // Intent intent = getIntent();
        // dialog = (QBDialog) intent.getSerializableExtra(EXTRA_DIALOG);
        // dialog = mactivity.getSelectedDialog();
		
		/*
		 * if (dialog.getType() == QBDialogType.GROUP) { TextView meLabel =
		 * (TextView) getView().findViewById(R.id.meLabel);
		 * container.removeView(meLabel); container.removeView(companionLabel);
		 * } else if (dialog.getType() == QBDialogType.PRIVATE) {
		 */
        //	Integer opponentID = ChatService.getInstance()
        //			.getOpponentIDForPrivateDialog(dialog);
        //	companionLabel.setText(ChatService.getInstance().getDialogsUsers()
        //			.get(opponentID).getLogin());
        // }

        // Send button
        //
        sendButton = (Button) getView().findViewById(R.id.chatSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                if (!flagLoadedChat) {
                    AppUtil.showCustomToast(context, "Please wait while " +
                            "we are connecting you to chat room..");
                } else {
                    sendChatMessage(messageText);
                }

            }
        });

        // Stickers
  /* amr		keyboardHandleLayout = (KeyboardHandleRelativeLayout) getView()
				.findViewById(R.id.sizeNotifierLayout);
		keyboardHandleLayout.listener = this;
		stickersFrame = getView().findViewById(R.id.frame);
		stickerButton = (ImageView) getView()
				.findViewById(R.id.stickers_button);

		stickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isStickersFrameVisible) {
					showKeyboard();
					stickerButton
							.setImageResource(R.drawable.ic_action_insert_emoticon);
				} else {
					if (keyboardHandleLayout.isKeyboardVisible()) {
						keyboardHandleLayout
								.hideKeyboard(
										getActivity(),
										new KeyboardHandleRelativeLayout.OnKeyboardHideCallback() {
											@Override
											public void onKeyboardHide() {
												stickerButton
														.setImageResource(R.drawable.ic_action_keyboard);
												setStickersFrameVisible(true);
											}
										});
					} else {
						stickerButton
								.setImageResource(R.drawable.ic_action_keyboard);
						setStickersFrameVisible(true);
					}
				}
			}
		});

		updateStickersFrameParams();
		StickersFragment stickersFragment = (StickersFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.frame);
		if (stickersFragment == null) {
			stickersFragment = new StickersFragment.Builder()
					.setStickerPlaceholderColorFilterRes(
							android.R.color.darker_gray).build();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame, stickersFragment).commit();
		}
		stickersFragment.setOnStickerSelectedListener(stickerSelectedListener);
		stickersFragment
				.setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
					@Override
					public void onEmojiBackspaceClicked() {
						KeyEvent event = new KeyEvent(0, 0, 0,
								KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
								KeyEvent.KEYCODE_ENDCALL);
						messageEditText.dispatchKeyEvent(event);
					}
				});
		setStickersFrameVisible(isStickersFrameVisible); */

    }

    private void showKeyboard() {
        ((InputMethodManager) messageEditText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(messageEditText,
                InputMethodManager.SHOW_IMPLICIT);
    }

    private void sendChatMessage(String messageText) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(messageText);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(new Date().getTime() / 1000);

        try {
            chat.sendMessage(chatMessage);
        } catch (XMPPException e) {
            Log.e("", "failed to send a message", e);
        } catch (SmackException sme) {
            Log.e("*", "failed to send a message", sme);
        }

        messageEditText.setText("");

        if (dialog.getType() == QBDialogType.PRIVATE) {
            showMessage1(chatMessage);
        }
    }

/* amr	private OnStickerSelectedListener stickerSelectedListener = new OnStickerSelectedListener() {
		@Override
		public void onStickerSelected(String code) {
			if (StickersManager.isSticker(code)) {
				sendChatMessage(code);
				// setStickersFrameVisible(false);
			} else {
				// append emoji to edit
				messageEditText.append(code);
			}
		}
	};

	@Override
	public void onKeyboardVisibilityChanged(boolean isVisible) {
		if (isVisible) {
			setStickersFrameVisible(false);
			stickerButton
					.setImageResource(R.drawable.ic_action_insert_emoticon);
		} else {
			if (isStickersFrameVisible) {
				stickerButton.setImageResource(R.drawable.ic_action_keyboard);
			} else {
				stickerButton
						.setImageResource(R.drawable.ic_action_insert_emoticon);
			}
		}
	}

	private void setStickersFrameVisible(final boolean isVisible) {
		stickersFrame.setVisibility(isVisible ? View.VISIBLE : View.GONE);
		isStickersFrameVisible = isVisible;
		if (stickersFrame.getHeight() != vc908.stickerfactory.utils.KeyboardUtils
				.getKeyboardHeight()) {
			updateStickersFrameParams();
		}
		final int padding = isVisible ? vc908.stickerfactory.utils.KeyboardUtils
				.getKeyboardHeight() : 0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			keyboardHandleLayout.post(new Runnable() {
				@Override
				public void run() {
					setContentBottomPadding(padding);
					scrollDown();
				}
			});
		} else {
			setContentBottomPadding(padding);
		}
		scrollDown();
	}

	private void updateStickersFrameParams() {
		stickersFrame.getLayoutParams().height = vc908.stickerfactory.utils.KeyboardUtils
				.getKeyboardHeight();
	}*/

    public void setContentBottomPadding(int padding) {
        container.setPadding(0, 0, 0, padding);
    }

    private void initChat() {

		/*
		 * if (dialog.getType() == QBDialogType.GROUP) { chat = new
		 * GroupChatImpl(getActivity());
		 * 
		 * // Join group chat // progressBar.setVisibility(View.VISIBLE); //
		 * joinGroupChat();
		 * 
		 * } else
		 */
        try {
            if (dialog.getType() == QBDialogType.PRIVATE) {
                Integer opponentID = ChatService.getInstance()
                        .getOpponentIDForPrivateDialog(dialog);

                chat = new PrivateChatImpl((ProductListActivity) getActivity(),
                        opponentID);

                // Load CHat history
                //
                loadChatHistory();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void joinGroupChat() {
        ((GroupChatImpl) chat).joinGroupChat(dialog,
                new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {

                        // Load Chat history
                        //
                        loadChatHistory();
                    }

                    @Override
                    public void onError(List list) {
                        // AlertDialog.Builder dialog = new
                        // AlertDialog.Builder(ChatActivity.this);
                        // dialog.setMessage("error when join group chat: " +
                        // list.toString()).create().show();
                    }
                });
    }

    private void loadChatHistory() {
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        customObjectRequestBuilder.sortDesc("date_sent");

        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder,
                new QBEntityCallbackImpl<ArrayList<QBChatMessage>>() {
                    @Override
                    public void onSuccess(ArrayList<QBChatMessage> messages,
                                          Bundle args) {

                        adapter = new ChatAdapter(getActivity(),
                                new ArrayList<QBChatMessage>());
                        messagesContainer.setAdapter(adapter);

                        for (int i = messages.size() - 1; i >= 0; --i) {
                            QBChatMessage msg = messages.get(i);
                            showMessage1(msg);
                        }
                        flagLoadedChat = true;
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        if (!getActivity().isFinishing()) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    context);
                            dialog.setMessage(
                                    "load chat history errors: " + errors)
                                    .create().show();
                        }
                    }
                });
    }

    public void showMessage1(QBChatMessage message) {
        if (adapter != null)
            adapter.add(message);
        Log.e("Run Fragment menthod", "**");
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    scrollDown();
                }
            });
        }
    }

    public void getSellerId(String seller_id) {

        Log.e("Seller_id in  chat------>", seller_id);
        sellerId = seller_id;
		/*
		 * adapter.add(message); Log.e("Run Fragment menthod", "**");
		 * getActivity().runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { adapter.notifyDataSetChanged();
		 * scrollDown(); } });
		 */
    }

    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    ConnectionListener chatConnectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {
            Log.i("", "connected");
        }

        @Override
        public void authenticated(XMPPConnection connection) {
            Log.i("", "authenticated");
        }

        @Override
        public void connectionClosed() {
            Log.i("", "connectionClosed");
        }

        @Override
        public void connectionClosedOnError(final Exception e) {
            Log.i("", "connectionClosedOnError: " + e.getLocalizedMessage());

            // leave active room
            //

        }

        @Override
        public void reconnectingIn(final int seconds) {
            if (seconds % 5 == 0) {
                Log.i("", "reconnectingIn: " + seconds);
            }
        }

        @Override
        public void reconnectionSuccessful() {
            Log.i("", "reconnectionSuccessful");

            // Join active room
            //

        }

        @Override
        public void reconnectionFailed(final Exception error) {
            Log.i("", "reconnectionFailed: " + error.getLocalizedMessage());
        }
    };

    private class AddDialogTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.addDialog(urls[0], urls[1], urls[2], context
                        .getResources().getString(R.string.add_dialog_url));
                Log.e("GetDialog", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {

                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {

                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "Please check your internet connection");
                }
                if (dialog1 != null)
                    dialog1.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog1 != null)
                    dialog1.cancel();
            }
        }
    }

    /**
     * get dialog id AsynTask
     */
    private class GetDialogTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getDialog(urls[0], urls[1], context
                        .getResources().getString(R.string.get_dialog_url));
                Log.e("Get Dialog Id Task Response", "***" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {

                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        JSONObject jo = job.getJSONObject("data");
                        dialogId = jo.getString("ChatDialogID");

                    }
                    try {
                        if (((BaseActivity) getActivity()).isSessionActive()) {
                            getDialogs();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "Please check your internet connection");
                }
                if (dialog1 != null)
                    dialog1.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog1 != null)
                    dialog1.cancel();
            }
        }
    }
}

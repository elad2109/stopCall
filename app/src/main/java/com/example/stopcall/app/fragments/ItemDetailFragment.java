package com.example.stopcall.app.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.stopcall.app.R;
import com.example.stopcall.app.dal.CommentDal;
import com.example.stopcall.app.dal.PhoneDal;
import com.example.stopcall.app.dal.dto.Phone;
import com.google.inject.Inject;

import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link com.example.stopcall.app.archive.ItemListActivity}
 * in two-pane mode (on tablets) or a {@link com.example.stopcall.app.activities.ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";


    @Inject private CommentDal commentDal;
    @Inject private PhoneDal phoneDal;
    private ArrayAdapter<String> arrayAdapter;
    private View rootView;
    private ListView listView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {

    }

    public void init(Context applicationContext) {
        this.phoneDal = new PhoneDal(applicationContext);
        this.commentDal = new CommentDal(applicationContext);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        setCommentsVisibility(rootView, false);

        listView = ((ListView) rootView.findViewById(R.id.comments_list));
        registerForContextMenu(listView);

        Button searchButton = (Button) rootView.findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneComments(rootView);
                setCommentsVisibility(rootView, true);
            }
        });

        Button addButton = (Button) rootView.findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = ((EditText) rootView.findViewById(R.id.phone_editText)).getText().toString();
                Phone newPhone = phoneDal.getItem(phoneNumber);
                long phoneNewId;

                if (newPhone == null) {
                    newPhone = new Phone();
                    newPhone.phone = phoneNumber;
                    newPhone.isBlocked = ((CheckBox) rootView.findViewById(R.id.isBlocked_cb)).isChecked();
                    phoneNewId = phoneDal.addItem(newPhone);
                } else {
                    phoneNewId = newPhone.id;
                }
                if (phoneNewId != 0) {
                    String comment = ((EditText) rootView.findViewById(R.id.comment_text)).getText().toString();
                    if (commentDal.addItem(phoneNewId, comment)) {
                        refreshList(comment);
                    }
                }

            }
        });
        return rootView;
    }

    private void refreshList(String comment) {
        if (arrayAdapter == null)
        {
            arrayAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1);
        }
        arrayAdapter.add(comment);
        listView.invalidate();
    }

    private void setCommentsVisibility(View rootView, boolean isVisible) {
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.bottom_container);
        relativeLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void getPhoneComments(View rootView) {
        String phoneNumber = ((EditText) rootView.findViewById(R.id.phone_editText)).getText().toString();
        Phone phone = phoneDal.getItem(phoneNumber);
        if (phone != null) {
            List<String> comments = commentDal.getAllItems(phone.id);

            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            arrayAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    comments);

            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.comments_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.phone_list_contextual_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
//            case R.id.add:
//                // add stuff here
//                return true;
            case R.id.edit: {
                // edit stuff here
                return true;
            }
            case R.id.delete: {
                // remove stuff here
                return true;
            }
            default: {
                return super.onContextItemSelected(item);
            }
        }
    }



}

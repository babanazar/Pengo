package com.example.pengout.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class SearchActivity extends AppCompatActivity {

    //private ImageView music,sport,edu,theatre;
    private ImageButton searchButton;
    private EditText searchText;
    private RecyclerView results;
    private DatabaseReference mEventDatabase;

    FirebaseRecyclerAdapter<Event, EventsViewHolder> adapter;
    Query firebaseSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cards);

        mEventDatabase = FirebaseDatabase.getInstance().getReference("EventsTranslated");

        searchButton = findViewById(R.id.search_btn);
        searchText = findViewById(R.id.search_field);
        results = findViewById(R.id.results);
        results.setLayoutManager(new LinearLayoutManager(this));

        //Glide.with(getApplicationContext()).load("https://cdn2.allevents.in/thumbs/thumb5dbb7cb95ea77.jpg").override(200,200).centerCrop().into(edu);
        //Glide.with(getApplicationContext()).load(R.drawable.theatre).override(200,200).into(theatre);
        //Glide.with(getApplicationContext()).load(R.drawable.sport_logo).override(200,200).into(sport);
        //Glide.with(getApplicationContext()).load(R.drawable.music).override(200,200).into(music);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String st = searchText.getText().toString();
                firebaseEventSearch(st);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseEventSearch("G");
    }

    void firebaseEventSearch(String searchText){
        Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
        firebaseSearchQuery = mEventDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Event>().setQuery(firebaseSearchQuery,Event.class).build();
        adapter = new FirebaseRecyclerAdapter<Event, EventsViewHolder>(options) {
            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchlist_layout,viewGroup,false);
                return new EventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull Event model) {
                holder.n.setText(model.getName());
                holder.d.setText(model.getDate());
                holder.p.setText(model.getPlace());
            }
        };
        results.setAdapter(adapter);
        adapter.startListening();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder{

        TextView n,d,p;
        public EventsViewHolder(View itemView){
            super(itemView);
            n = itemView.findViewById(R.id.name_text);
            d = itemView.findViewById(R.id.date_text);
            p = itemView.findViewById(R.id.place_text);

        }
    }

}

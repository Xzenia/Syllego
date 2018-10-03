package test.omegaware.syllego;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookContentListAdapter extends RecyclerView.Adapter<BookContentListAdapter.ViewHolder> {

    private ArrayList<Book> bookList;
    private Context mContext;

    public BookContentListAdapter (Context context, ArrayList<Book> bookList){
        this.bookList = bookList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookcontentlist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.BookNameTextView.setText(bookList.get(position).getBookName());
        holder.BookAuthorTextView.setText(bookList.get(position).getBookAuthor());
        holder.YearReleasedTextView.setText(bookList.get(position).getYearReleased());
        holder.BookContentItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book selectedBook = bookList.get(position);
                viewSelectedBook(selectedBook);
            }
        });
    }

    public void viewSelectedBook(Book selectedBook){
        Intent goToViewBook = new Intent(mContext, ViewBook.class);
        goToViewBook.putExtra("SelectedBook", selectedBook);
        mContext.startActivity(goToViewBook);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView BookNameTextView;
        TextView BookAuthorTextView;
        TextView YearReleasedTextView;
        RelativeLayout BookContentItemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            BookNameTextView = itemView.findViewById(R.id.BookName);
            BookAuthorTextView = itemView.findViewById(R.id.BookAuthor);
            YearReleasedTextView = itemView.findViewById(R.id.BookYearReleased);
            BookContentItemLayout = itemView.findViewById(R.id.ItemListRelativeLayout);
        }
    }
}

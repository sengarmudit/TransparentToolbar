package com.ostopd.transparenttoolbar.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;
import com.ostopd.transparenttoolbar.OnItemClickListener;
import com.ostopd.transparenttoolbar.R;
import com.ostopd.transparenttoolbar.models.Book;
import com.ostopd.transparenttoolbar.views.Utils;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BooksViewHolder> {

    private final ArrayList<Book> books;
    private Context context;
    private int defaultBackgroundcolor;
    private OnItemClickListener onItemClickListener;
    private int lastPosition = -1;
    private static final int SCALE_DELAY = 30;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BookAdapter(ArrayList<Book> books) {

        this.books = books;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup viewGroup,  int position) {

        View rowView = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_book, viewGroup, false);

        this.context = viewGroup.getContext();
        defaultBackgroundcolor = context.getResources().getColor(R.color.book_without_palette);

        return new BooksViewHolder(rowView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final BooksViewHolder booksViewHolder, final int position) {

        final Book currentBook = books.get(position);
        booksViewHolder.bookTitle.setText(currentBook.getTitle());
        booksViewHolder.bookAuthor.setText(currentBook.getAuthor());
        booksViewHolder.bookCover.setDrawingCacheEnabled(true);

        Ion.with(context)
            .load(books.get(position).getCover())
            .intoImageView(booksViewHolder.bookCover)
            .withBitmapInfo()
            .setCallback(new FutureCallback<ImageViewBitmapInfo>() {
                @Override
                public void onCompleted(Exception e, ImageViewBitmapInfo result) {

                    if (e == null && result != null) {

                        setCellColors(result.getBitmapInfo().bitmap, booksViewHolder, position);
                        amimateCell(booksViewHolder);
                    }
                }
            });
    }
    private static final int MAX_PHOTO_ANIMATION_DELAY = 100;
    private long profileHeaderAnimationStartTime = 0;

    private void amimateCell(BooksViewHolder booksViewHolder) {

        int cellPosition = booksViewHolder.getPosition();

        if (!booksViewHolder.animated) {

            booksViewHolder.animated = true;
            booksViewHolder.bookContainer.setScaleY(0);
            booksViewHolder.bookContainer.setScaleX(0);
            booksViewHolder.bookContainer.animate()
                .scaleY(1).scaleX(1)
                .setDuration(200)
                .setStartDelay(SCALE_DELAY * cellPosition)
                .start();
        }

    }


    public void setCellColors (Bitmap b, final BooksViewHolder viewHolder, final int position) {

        if (b != null) {
            Palette.generateAsync(b, new Palette.PaletteAsyncListener() {

                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

                    if (vibrantSwatch != null) {

                        viewHolder.bookTitle.setTextColor(vibrantSwatch.getTitleTextColor());
                        viewHolder.bookAuthor.setTextColor(vibrantSwatch.getTitleTextColor());
                        viewHolder.bookCover.setTransitionName("cover" + position);
                        viewHolder.bookTextcontainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onItemClickListener.onClick(v, position);
                            }
                        });

                        Utils.animateViewColor(viewHolder.bookTextcontainer, defaultBackgroundcolor,
                            vibrantSwatch.getRgb());

                    } else {

                        Log.e("[ERROR]", "BookAdapter onGenerated - The VibrantSwatch were null at: " + position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return books.size();
    }
}

class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected  LinearLayout bookContainer;
    protected RelativeLayout bookTextcontainer;
    protected ImageView bookCover;
    protected TextView bookTitle;
    protected TextView bookAuthor;
    private OnItemClickListener onItemClickListener;
    protected boolean animated = false;

    public BooksViewHolder(View itemView, OnItemClickListener onItemClickListener) {

        super(itemView);
        this.onItemClickListener = onItemClickListener;

        bookContainer = (LinearLayout) itemView.findViewById(R.id.item_book_container);
        bookTextcontainer = (RelativeLayout) itemView.findViewById(R.id.item_book_text_container);
        bookCover = (ImageView) itemView.findViewById(R.id.item_book_img);
        bookTitle = (TextView) itemView.findViewById(R.id.item_book_title);
        bookAuthor = (TextView) itemView.findViewById(R.id.item_book_author);
        bookCover.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        onItemClickListener.onClick(v, getPosition());

    }
}


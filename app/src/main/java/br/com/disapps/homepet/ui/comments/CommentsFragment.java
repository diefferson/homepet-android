package br.com.disapps.homepet.ui.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.disapps.homepet.R;
import br.com.disapps.homepet.app.HomePet;
import br.com.disapps.homepet.data.model.Comment;
import br.com.disapps.homepet.ui.comments.adapter.CommentAdapter;
import br.com.disapps.homepet.ui.common.AppFragment;
import butterknife.BindView;

/**
 * Created by diefferson.santos on 31/08/17.
 */

public class CommentsFragment extends AppFragment<ICommentsView, CommentsPresenter> implements ICommentsView {

    @BindView(R.id.comments_recycler)
    RecyclerView commentsRecycler;

    @BindView(R.id.loading_view)
    ConstraintLayout loadingView;

    private CommentAdapter commentAdapter;

    public static CommentsFragment newInstance(int codeHotel){

        CommentsFragment commentsFragment = new CommentsFragment();

        Bundle args = new Bundle();
        args.putInt("codeHotel", codeHotel);
        commentsFragment.setArguments(args);

        return commentsFragment;
    }

    @Override
    public String getFragmentTag() {
        return CommentsFragment.class.getSimpleName();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_comments;
    }

    @Override
    public CommentsPresenter createPresenter() {
        return new CommentsPresenter(HomePet.Companion.getInstance().getHoteltRepository());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLoadingFragment(loadingView);

        int codeHotel = getArguments().getInt("codeHotel");

        getPresenter().loadComments(codeHotel);
    }

    @Override
    public void fillComments(List<Comment> comments) {
        commentAdapter = new CommentAdapter(comments);
        commentsRecycler.setAdapter(commentAdapter);
    }

}

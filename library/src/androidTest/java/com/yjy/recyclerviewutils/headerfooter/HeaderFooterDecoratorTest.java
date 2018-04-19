package com.yjy.recyclerviewutils.headerfooter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class HeaderFooterDecoratorTest {

    private Context mAppContext;

    @Before
    public void setUp() throws Exception {
        mAppContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testIllegalArgumentException() {
        assertIllegalArgumentException(() -> createCorrectDecorator()
                .setAdapter(null)
                .decorate(createRecyclerView()));

        assertIllegalArgumentException(() -> createCorrectDecorator()
                .setLayoutManager(new LinearLayoutManager(mAppContext, LinearLayoutManager.HORIZONTAL, false))
                .decorate(createRecyclerView()));

        assertIllegalArgumentException(() -> createCorrectDecorator()
                .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL))
                .decorate(createRecyclerView()));
    }

    private void assertIllegalArgumentException(Runnable runnable) {
        try {
            runnable.run();
        } catch (IllegalArgumentException ignored) {
            return;
        }

        fail();
    }

    private HeaderFooterDecorator createCorrectDecorator() {
        return new HeaderFooterDecorator()
                .setAdapter(createAdapter())
                .setLayoutManager(new LinearLayoutManager(mAppContext));
    }

    private RecyclerView createRecyclerView() {
        return new RecyclerView(mAppContext);
    }

    @NonNull
    private RecyclerView.Adapter createAdapter() {
        return new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
    }
}
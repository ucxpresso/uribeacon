/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uribeacon.validator;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder>{
  private ArrayList<TestHelper> mDataset;
  private static final String TAG = TestsAdapter.class.getCanonicalName();
  // Provide a reference to the views for each data item
  // Complex data items may need more than one view per item, and
  // you provide access to all the views for a data item in a view holder
  public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mTestName;
    public TextView mTestResult;
    public ImageView mImageView;

    public ViewHolder(View v) {
      super(v);
      mTestName = (TextView) v.findViewById(R.id.test_name);
      mTestResult = (TextView) v.findViewById(R.id.test_reason);
      mImageView = (ImageView) v.findViewById(R.id.imageView_testIcon);

    }
  }

  // Provide a suitable constructor (depends on the kind of dataset)
  public TestsAdapter(ArrayList<TestHelper> uriBeaconTests){
    mDataset = uriBeaconTests;
  }
  // Create new views (invoked by the layout manager)
  @Override
  public TestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.test_view, parent, false);
    // set <></>he view's size, margins, paddings and layout parameters
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    TestHelper test = mDataset.get(position);
    holder.mTestName.setText(test.getName());
    setIcon(holder.mImageView, test);
    setErrorMessage(holder, test);
  }

  private void setErrorMessage(ViewHolder holder, TestHelper test) {
    if (test.isFailed()) {
      for (int i = 0; i < test.getTestSteps().size(); i++) {
        TestAction action = test.getTestSteps().get(i);
        if (action.failed) {
          holder.mTestResult.setText("#" + (i + 1) + ". " + action.reason);
          holder.mTestResult.setVisibility(View.VISIBLE);
          break;
        }
      }
    }
  }

  private void setIcon(ImageView imageView, TestHelper test) {
    if (!test.isStarted()) {
      imageView.setImageResource(R.drawable.not_started);
    } else if (!test.isFinished()) {
      imageView.setImageResource(R.drawable.executing_animated);
      ((AnimatedVectorDrawable) imageView.getDrawable()).start();
    } else if (!test.isFailed()) {
      imageView.setImageResource(R.drawable.success);
    } else {
      imageView.setImageResource(R.drawable.failed);
    }
  }
  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return mDataset.size();
  }
}

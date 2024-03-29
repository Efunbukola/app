/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ObservableScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saboorsalaam.veed10.R;


public class ViewPagerTab2GridViewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);

        Activity parentActivity = getActivity();
        final ObservableGridView gridView = (ObservableGridView) view.findViewById(R.id.scroll);
        gridView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            gridView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }
}

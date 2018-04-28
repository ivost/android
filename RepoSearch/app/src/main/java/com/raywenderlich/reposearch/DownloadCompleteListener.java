package com.raywenderlich.reposearch;

import java.util.ArrayList;

/**
 * Created by mattluedke on 5/10/16.
 */
public interface DownloadCompleteListener {
  void downloadComplete(ArrayList<Repository> repositories);
}

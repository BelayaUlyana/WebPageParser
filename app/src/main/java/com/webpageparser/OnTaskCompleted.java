package com.webpageparser;

import java.util.List;

public interface OnTaskCompleted {
    void onTaskCompleted(List<String> responseLinksList, List<String> responseEmailsList);
}
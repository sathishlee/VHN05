package com.unicef.vhn.model.ChildDevelopmentModdel;

import java.util.ArrayList;

public class OuterListModel extends ArrayList<ChildQuestionReportViewModel> {
    private ArrayList<ChildQuestionReportViewModel> childQuestionReportViewModels;


    public ArrayList<ChildQuestionReportViewModel> getChildQuestionReportViewModels() {

        return childQuestionReportViewModels;
    }

    public void setChildQuestionReportViewModels(ArrayList<ChildQuestionReportViewModel> childQuestionReportViewModels) {
        this.childQuestionReportViewModels = childQuestionReportViewModels;
    }

}

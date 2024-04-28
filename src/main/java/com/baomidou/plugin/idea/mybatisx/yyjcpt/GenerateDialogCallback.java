package com.baomidou.plugin.idea.mybatisx.yyjcpt;

import javax.swing.*;

public class GenerateDialogCallback {
    private final Action okAction;
    private boolean lastStep = false;

    public GenerateDialogCallback(Action okAction) {
        this.okAction = okAction;
    }

    public void updateStepStatus(boolean last) {
        if (lastStep != last) {
            lastStep = last;
            this.okAction.putValue("Name", lastStep ? "Finish" : "Next");
        }
    }

    public boolean isLastStep() {
        return lastStep;
    }
}

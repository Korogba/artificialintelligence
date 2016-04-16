package ai.kaba.abstracts.interfaces;

import javax.swing.*;

/**
 * Created by Yusuf on 4/12/2016
 * Interface to link all components that have algorithms running
 */
public interface Runner {
    JButton getClear();
    JButton getSearch();
    void allStatus(boolean flag);
    void disableExceptClear();
}
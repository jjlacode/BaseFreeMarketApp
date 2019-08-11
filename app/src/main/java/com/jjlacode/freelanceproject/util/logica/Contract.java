package com.jjlacode.freelanceproject.util.logica;

import com.jjlacode.freelanceproject.util.BasePresenter;
import com.jjlacode.freelanceproject.util.BaseView;

public interface Contract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}

package jjlacode.com.freelanceproject.util.logica;

import jjlacode.com.freelanceproject.util.BasePresenter;
import jjlacode.com.freelanceproject.util.BaseView;

public interface Contract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}

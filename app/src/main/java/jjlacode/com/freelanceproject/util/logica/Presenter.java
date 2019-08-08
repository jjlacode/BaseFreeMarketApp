package jjlacode.com.freelanceproject.util.logica;

import androidx.annotation.NonNull;

public class Presenter implements Contract.Presenter, Interactor.Callback {

    private final Contract.View mView;
    private Interactor mInteractor;

    public Presenter(@NonNull Contract.View view,
                     @NonNull Interactor interactor) {
        mView = view;
        view.setPresenter(this);
        mInteractor = interactor;
    }

    @Override
    public void start() {

    }
}

package jjlacode.com.freelanceproject.util.notifications;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.BasePresenter;
import jjlacode.com.freelanceproject.util.BaseView;
import jjlacode.com.freelanceproject.util.data.PushNotification;

/**
 * Interacción MVP en Notificaciones
 */
public interface PushNotificationContract {

    interface View extends BaseView<Presenter> {

        void showNotifications(ArrayList<PushNotification> notifications);

        void showEmptyState(boolean empty);

        void popPushNotification(PushNotification pushMessage);
    }

    interface Presenter extends BasePresenter {

        void registerAppClient();

        void loadNotifications();

        void savePushMessage(String title, String description,
                             String expiryDate, String discount);
    }
}

package com.codevsolution.base.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

import java.util.Objects;

public class CamaraUtil {

    private CameraDevice camera;
    private CameraManager manager;

    public CamaraUtil(final Context context) {
        this.manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);


    }

    @SuppressLint("MissingPermission")
    public void open(final int cameraId) {

        System.out.println("cameraId = " + cameraId);
        try {
            String[] cameraIds = manager.getCameraIdList();
            manager.openCamera(cameraIds[cameraId], new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    CamaraUtil.this.camera = camera;
                    System.out.println("camera = " + camera.getId());
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    CamaraUtil.this.camera = camera;
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    CamaraUtil.this.camera = camera;
                    System.out.println("error camara= " + error);
                }
            }, null);
        } catch (Exception e) {
        }
    }

    public CameraDevice getCamera() {
        return camera;
    }

    public int getOrientation(final int cameraId) {
        try {
            String[] cameraIds = manager.getCameraIdList();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[cameraId]);
            return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            return 0;
        }
    }

    public static int getIdCameraBack(Context context) {

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String[] cameraIds = manager.getCameraIdList();
            System.out.println("cameraIds = " + cameraIds.length);
            for (int i = 0; i < cameraIds.length; i++) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[i]);
                if (Objects.equals(characteristics.get(CameraCharacteristics.LENS_FACING), CameraCharacteristics.LENS_FACING_BACK)) {
                    return i;
                }

            }
        } catch (CameraAccessException e) {

        }
        return 0;
    }

    public static int getIdCameraFront(Context context) {

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = manager.getCameraIdList();
            System.out.println("cameraIds = " + cameraIds.length);

            for (int i = 0; i < cameraIds.length; i++) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[i]);
                if (Objects.equals(characteristics.get(CameraCharacteristics.LENS_FACING), CameraCharacteristics.LENS_FACING_FRONT)) {
                    return i;
                }

            }
        } catch (CameraAccessException e) {

        }
        return 0;
    }

}

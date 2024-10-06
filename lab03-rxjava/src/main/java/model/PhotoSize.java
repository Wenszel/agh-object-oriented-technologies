package model;

public enum PhotoSize {

    SMALL, MEDIUM, LARGE;

    private static final int SMALL_PHOTO_FILE_SIZE = 100 * 1024; // 100kB

    private static final int MEDIUM_PHOTO_FILE_SIZE = 2000 * 1024; // 2MB

    public static PhotoSize resolve(Photo photo) {
        int photoFileSize = photo.getPhotoData().length;
        if(photoFileSize <= SMALL_PHOTO_FILE_SIZE) {
            return SMALL;
        } else if (photoFileSize <= MEDIUM_PHOTO_FILE_SIZE) {
            return MEDIUM;
        }
        return LARGE;
    }
}

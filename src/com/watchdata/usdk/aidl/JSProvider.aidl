package com.watchdata.usdk.aidl;

interface JSProvider {

    String getServiceVersion();

    /**
     * get printer status
     *
     * @param [in] printer_id
     *
     * @return  0: ok
                ERROR_JS_PRINTER_NOT_EXISTED: printer not existed
                ERROR_JS_PRINTER_NOT_CONNECTED: printer not connected
                ERROR_JS_PRINTER_BUSY: printer busy
                ERROR_JS_PRINTER_PAPER_LACK: paper lack
                ERROR_JS_PRINTER_TOO_HOT: too hot
    */
    int printer_status(int printer_id);

    /**
     * set parameter
     *
     * @param [in] printer_id
     * @param [in] param_id: parameter id
                      PRINTER_PARAMID_WEBVIEW_MODE: webview mode
                              PRINTER_WEBVIEW_MODE1: unsupported now
                              PRINTER_WEBVIEW_MODE2: by default
                      PRINTER_PARAMID_GRAY_SCALE: gray scale,
                              arg1=value
                      PRINTER_PARAMID_BITMAP_NAME: bitmap name random or fixed,
                              arg1=0: fixed(default),
                              arg1=1: random

     * @return 0: successful
               ERROR_JS_PRINTER_NOT_EXISTED: printer not existed
               ERROR_JS_PRINTER_UNKNOWN_PARAMETER_ID: unknown parameter id
               ERROR_JS_PRINTER_INVALID_ARG1: invalid arg1
               ERROR_JS_PRINTER_INVALID_ARG2: invalid arg2
     */
    int printer_setparam(int printer_id, int param_id, int arg1, String arg2);

    /**
     * print the html_context
     *
     * @param [in] printer_id
     * @param [in] html_cotext: context to be printed
     * @param [in] dummy_print: true: do print, false: generate bitmap only
     * @param [in] savebmp: true: save the bitmap to SDCard, false: won't save
     *
     * @return: result string
           format := return_code | printed_height | bitmap file pathname | return_msg
           return_code:
                0: ok
                ERROR_JS_PRINTER_NOT_EXISTED: printer not existed
                ERROR_JS_PRINTER_NOT_CONNECTED: printer not connected
                ERROR_JS_PRINTER_BUSY: printer busy
                ERROR_JS_PRINTER_PAPER_LACK: paper lack
                ERROR_JS_PRINTER_TOO_HOT: too hot
           e.g "0|254|/sdcard/bmp/voucher.bmp|print successful"
     */
    String printer_drawhtml(int printer_id, String html_cotext, boolean dummy_print, boolean savebmp);

    /**
     * feed line
     *
     * @param [in] printer_id
     * @param [in] pixel: line to feed (unit: pixel)
     *
     * @return 0: successful
     */
    int printer_feedline(int printer_id, int pixel);
}

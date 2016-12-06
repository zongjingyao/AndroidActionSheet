# AndroidActionSheet
ActionSheet on Android

## Usage
### Add dependency
```
dependencies {
    compile 'com.github.zongjingyao:actionsheet:0.1.3'
}
```
### Example
```
ActionSheet actionSheet = new ActionSheet.Builder()
        .setTitle("Title", Color.BLUE)
        //.setTitleTextSize(20)
        .setOtherBtn(new String[]{"Btn0", "Btn1", "Btn2"}, new int[]{Color.BLACK, Color.GREEN, Color.GREEN})
        //.setOtherBtnTextSize(30)
        .setOtherBtnSub(new String[]{null, "Btn1 sub", ""}, new int[]{Color.BLACK, Color.GRAY, Color.GREEN})
        //.setOtherBtnSubTextSize(20)
        .setCancelBtn("Cancel", Color.RED)
        //.setCancelBtnTextSize(30)
        .setCancelableOnTouchOutside(true)
        .setActionSheetListener(new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                Toast.makeText(MainActivity.this, "onDismiss: " + isByBtn, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onButtonClicked(ActionSheet actionSheet, int index) {
                Toast.makeText(MainActivity.this, "onButtonClicked: " + index, Toast.LENGTH_SHORT).show();
            }
        }).build();

actionSheet.show(getFragmentManager());
```

## Screenshot
![screenshot](https://raw.githubusercontent.com/zongjingyao/AndroidActionSheet/master/screenshot/example.png)
![screenshot](https://raw.githubusercontent.com/zongjingyao/AndroidActionSheet/master/screenshot/example1.png)

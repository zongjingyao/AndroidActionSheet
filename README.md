# AndroidActionSheet
ActionSheet on Android

## Usage
### Add dependency
```
dependencies {
    compile 'com.github.zongjingyao:actionsheet:0.1'
}
```
### Example
```
ActionSheet actionSheet = new ActionSheet.Builder()
    .setTitle("Title", Color.BLUE)
    .setOtherBtn(new String[]{"Btn0", "Btn1", "Btn2"}, new int[]{Color.BLACK, Color.GREEN, Color.GREEN})
    .setCancelBtn("Cancel", Color.RED)
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

## ! Under construction !

# A simple modern share dialog for Android

This project aims to provide developers a simple modern share dialog.

![alt tag](https://raw.github.com/turbo2pointo/BetterShare/master/demo.png)

## Features
- Singleton implementation - avoid multi instances from accidental double click
- Dialog fragment implementation - safely handled orientation changes
- Prioritize commonly used share activities
- Prioritize last used share activitiey
More to come

## Usage

``` java
    DialogFrag_BetterShare.setup("BetterShare Subject", "BetterShare Text", true);
    DialogFrag_BetterShare dialogFrag_betterShare = DialogFrag_BetterShare.getInstance();
    dialogFrag_betterShare.show(getFragmentManager(), TAG_SHARE_DIALOG_FRAGMENT);
```
## License
Copyright (c) 2014 Benny Chau 

If you use Simple Social Sharing code in your application you should inform the author about it (*email: benny.chaucc[at]gmail[dot]com*). Also you should (but you don't have to) mention it in application UI with string **"Used BetterShare (c) 2014, Benny Chau"** (e.g. in some "About" section).

Licensed under the [BSD 3-clause](http://www.opensource.org/licenses/BSD-3-Clause)
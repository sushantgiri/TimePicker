# Date Time Picker
- The simple and powerful date time picker for Android.

<img src="https://user-images.githubusercontent.com/22026902/55102879-7b723980-50ef-11e9-9aa9-41c169ce70e8.gif" width="200" height="360" />

##**Features:**
- Set to default current time.
- Set default value on start Up.
- Completely customizable views.

##**Implementation:**


1.Instantiate DateFragment Class as:
  - DateTimeFragment dateTimeFragment = DateTimeFragment.newInstance();

2. Customize the parameters:
 //View Customizations


  - setDateHeaderText(String text) -- Change Date Header Text 
  - setDateHeaderColor(@ColorInt int color) -- Change Date Header Color

Root Background (Choose either of the option)
 - setBackgroundResource(@DrawableRes int resource) 
 - setBackgroundColor(@ColorInt int color) 
 - setBackground(Drawable backgroundDrawable)


 - setTimeHeaderText(String text) -- Change Time Header
 - setTimeHeaderColor(@ColorInt int color) -- Change Time Header Color
 - setConfirmText(String text) -- Change Confirm Text
 - setConfirmTextColor(@ColorInt int color) -- Change Confirm Text Color

Confirm Button Background (Choose either of the option)
 - setConfirmBackground(Drawable drawable) -- Change Confirm Button 
 - setConfirmBackgroundResource(@DrawableRes int resource) 
 - setConfirmBackgroundColor(@ColorInt int confirmBackgroundColor)

Center Selected Color
 - setSelectedColor(@ColorInt int selectedColor) 

Remaining Unselected Color other than center text
 - setUnSelectedColor(@ColorInt int unSelectedColor) 

3. Add DateTime Fragment in container.

**SOURCE: FORKED (CHINA | चीन )**


 




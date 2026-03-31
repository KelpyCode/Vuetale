package li.kelp.vuetale.tree

import li.kelp.vuetale.validator.PropertyValidator

val elementTagMap = mutableMapOf<String, Class<out Element>>()

fun initializeElements() {
    // ActionButton element
    elementTagMap["actionbutton"] = ActionButtonElement::class.java
    PropertyValidator.registerProperties(ActionButtonElement::class.java, listOf("ActionName", "Alignment", "Anchor", "AutoScrollDown", "Background", "BindingModifier1Label", "BindingModifier2Label", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "IsAvailable", "IsHoldBinding", "KeepScrollPosition", "KeyBindingLabel", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // AssetImage element
    elementTagMap["assetimage"] = AssetImageElement::class.java
    PropertyValidator.registerProperties(AssetImageElement::class.java, listOf("Anchor", "AssetPath", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // BackButton element
    elementTagMap["backbutton"] = BackButtonElement::class.java
    PropertyValidator.registerProperties(BackButtonElement::class.java, listOf("ActionName", "Alignment", "Anchor", "AutoScrollDown", "Background", "BindingModifier1Label", "BindingModifier2Label", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "IsAvailable", "IsHoldBinding", "KeepScrollPosition", "KeyBindingLabel", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // BlockSelector element
    elementTagMap["blockselector"] = BlockSelectorElement::class.java
    PropertyValidator.registerProperties(BlockSelectorElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "Capacity", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // Button element
    elementTagMap["button"] = ButtonElement::class.java
    PropertyValidator.registerProperties(ButtonElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // CharacterPreviewComponent element
    elementTagMap["characterpreviewcomponent"] = CharacterPreviewComponentElement::class.java
    PropertyValidator.registerProperties(CharacterPreviewComponentElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // CheckBox element
    elementTagMap["checkbox"] = CheckBoxElement::class.java
    PropertyValidator.registerProperties(CheckBoxElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // CheckBoxContainer element
    elementTagMap["checkboxcontainer"] = CheckBoxContainerElement::class.java
    PropertyValidator.registerProperties(CheckBoxContainerElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // CircularProgressBar element
    elementTagMap["circularprogressbar"] = CircularProgressBarElement::class.java
    PropertyValidator.registerProperties(CircularProgressBarElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "Color", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // CodeEditor element
    elementTagMap["codeeditor"] = CodeEditorElement::class.java
    PropertyValidator.registerProperties(CodeEditorElement::class.java, listOf("Anchor", "AutoFocus", "AutoGrow", "AutoScrollDown", "AutoSelectAll", "Background", "ContentHeight", "ContentPadding", "ContentWidth", "Decoration", "FlexWeight", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "Language", "LineNumberBackground", "LineNumberPadding", "LineNumberTextColor", "LineNumberWidth", "MaskTexturePath", "MaxLength", "MaxLines", "MaxVisibleLines", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PlaceholderStyle", "PlaceholderText", "ScrollbarStyle", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // ColorOptionGrid element
    elementTagMap["coloroptiongrid"] = ColorOptionGridElement::class.java
    PropertyValidator.registerProperties(ColorOptionGridElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ColorOptions", "ColorsPerRow", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Selected", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ColorPickerDropdownBox element
    elementTagMap["colorpickerdropdownbox"] = ColorPickerDropdownBoxElement::class.java
    PropertyValidator.registerProperties(ColorPickerDropdownBoxElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "Color", "ContentHeight", "ContentWidth", "DisplayTextField", "FlexWeight", "Format", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ResetTransparencyWhenChangingColor", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // CompactTextField element
    elementTagMap["compacttextfield"] = CompactTextFieldElement::class.java
    PropertyValidator.registerProperties(CompactTextFieldElement::class.java, listOf("Anchor", "AutoFocus", "AutoScrollDown", "AutoSelectAll", "Background", "CollapsedWidth", "CollapseSound", "ContentHeight", "ContentWidth", "Decoration", "ExpandedWidth", "ExpandSound", "FlexWeight", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "MaxLength", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PasswordChar", "PlaceholderStyle", "PlaceholderText", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // DropdownBox element
    elementTagMap["dropdownbox"] = DropdownBoxElement::class.java
    PropertyValidator.registerProperties(DropdownBoxElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "DisplayNonExistingValue", "Entries", "FlexWeight", "ForcedLabel", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "MaxSelection", "MouseWheelScrollBehaviour", "NoItemsText", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PanelTitleText", "SelectedValues", "ShowLabel", "ShowSearchInput", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // DropdownEntry element
    elementTagMap["dropdownentry"] = DropdownEntryElement::class.java
    PropertyValidator.registerProperties(DropdownEntryElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Selected", "Style", "Text", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // DynamicPane element
    elementTagMap["dynamicpane"] = DynamicPaneElement::class.java
    PropertyValidator.registerProperties(DynamicPaneElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MinSize", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ResizeAt", "ResizerBackground", "ResizerSize", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // DynamicPaneContainer element
    elementTagMap["dynamicpanecontainer"] = DynamicPaneContainerElement::class.java
    PropertyValidator.registerProperties(DynamicPaneContainerElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // FloatSlider element
    elementTagMap["floatslider"] = FloatSliderElement::class.java
    PropertyValidator.registerProperties(FloatSliderElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "Max", "Min", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Step", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // FloatSliderNumberField element
    elementTagMap["floatslidernumberfield"] = FloatSliderNumberFieldElement::class.java
    PropertyValidator.registerProperties(FloatSliderNumberFieldElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "Max", "Min", "MouseWheelScrollBehaviour", "NumberFieldContainerAnchor", "NumberFieldDefaultValue", "NumberFieldMaxDecimalPlaces", "NumberFieldStyle", "NumberFieldSuffix", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "SliderStyle", "Step", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // Group element
    elementTagMap["group"] = GroupElement::class.java
    elementTagMap["div"] = GroupElement::class.java
    PropertyValidator.registerProperties(GroupElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // HotkeyLabel element
    elementTagMap["hotkeylabel"] = HotkeyLabelElement::class.java
    PropertyValidator.registerProperties(HotkeyLabelElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "InputBindingKey", "InputBindingKeyPrefix", "InputBindingKeyPrefixBinding", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ItemGrid element
    elementTagMap["itemgrid"] = ItemGridElement::class.java
    PropertyValidator.registerProperties(ItemGridElement::class.java, listOf("AdjacentInfoPaneGridWidth", "AllowMaxStackDraggableItems", "Anchor", "AreItemsDraggable", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "DisplayItemQuantity", "FlexWeight", "HitTestVisible", "InfoDisplay", "InventorySectionId", "ItemStacks", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "RenderItemQualityBackground", "ScrollbarStyle", "ShowScrollbar", "Slots", "SlotsPerRow", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ItemIcon element
    elementTagMap["itemicon"] = ItemIconElement::class.java
    PropertyValidator.registerProperties(ItemIconElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "ItemId", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ShowItemTooltip", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ItemPreviewComponent element
    elementTagMap["itempreviewcomponent"] = ItemPreviewComponentElement::class.java
    PropertyValidator.registerProperties(ItemPreviewComponentElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "ItemId", "ItemScale", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ItemSlot element
    elementTagMap["itemslot"] = ItemSlotElement::class.java
    PropertyValidator.registerProperties(ItemSlotElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "ItemId", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Quantity", "ShowDurabilityBar", "ShowQualityBackground", "ShowQuantity", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ItemSlotButton element
    elementTagMap["itemslotbutton"] = ItemSlotButtonElement::class.java
    PropertyValidator.registerProperties(ItemSlotButtonElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // Label element
    elementTagMap["label"] = LabelElement::class.java
    elementTagMap["span"] = LabelElement::class.java
    elementTagMap["p"] = LabelElement::class.java
    PropertyValidator.registerProperties(LabelElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "Text", "TextSpans", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // LabeledCheckBox element
    elementTagMap["labeledcheckbox"] = LabeledCheckBoxElement::class.java
    PropertyValidator.registerProperties(LabeledCheckBoxElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // MenuItem element
    elementTagMap["menuitem"] = MenuItemElement::class.java
    PropertyValidator.registerProperties(MenuItemElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "Icon", "IconAnchor", "IsSelected", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PopupStyle", "SelectedStyle", "Style", "Text", "TextSpans", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // MultilineTextField element
    elementTagMap["multilinetextfield"] = MultilineTextFieldElement::class.java
    PropertyValidator.registerProperties(MultilineTextFieldElement::class.java, listOf("Anchor", "AutoFocus", "AutoGrow", "AutoScrollDown", "AutoSelectAll", "Background", "ContentHeight", "ContentPadding", "ContentWidth", "Decoration", "FlexWeight", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "MaxLength", "MaxLines", "MaxVisibleLines", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PlaceholderStyle", "PlaceholderText", "ScrollbarStyle", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // NumberField element
    elementTagMap["numberfield"] = NumberFieldElement::class.java
    PropertyValidator.registerProperties(NumberFieldElement::class.java, listOf("Anchor", "AutoFocus", "AutoScrollDown", "AutoSelectAll", "Background", "ContentHeight", "ContentWidth", "Decoration", "FlexWeight", "Format", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "MaxLength", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PasswordChar", "PlaceholderStyle", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // Panel element
    elementTagMap["panel"] = PanelElement::class.java
    PropertyValidator.registerProperties(PanelElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ProgressBar element
    elementTagMap["progressbar"] = ProgressBarElement::class.java
    PropertyValidator.registerProperties(ProgressBarElement::class.java, listOf("Alignment", "Anchor", "AutoScrollDown", "Background", "Bar", "BarTexturePath", "ContentHeight", "ContentWidth", "Direction", "EffectHeight", "EffectOffset", "EffectTexturePath", "EffectWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // ReorderableList element
    elementTagMap["reorderablelist"] = ReorderableListElement::class.java
    PropertyValidator.registerProperties(ReorderableListElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "DropIndicatorAnchor", "DropIndicatorBackground", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ReorderableListGrip element
    elementTagMap["reorderablelistgrip"] = ReorderableListGripElement::class.java
    PropertyValidator.registerProperties(ReorderableListGripElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "IsDragEnabled", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "ScrollbarStyle", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // SceneBlur element
    elementTagMap["sceneblur"] = SceneBlurElement::class.java
    PropertyValidator.registerProperties(SceneBlurElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // Slider element
    elementTagMap["slider"] = SliderElement::class.java
    PropertyValidator.registerProperties(SliderElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "Max", "Min", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Step", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // SliderNumberField element
    elementTagMap["slidernumberfield"] = SliderNumberFieldElement::class.java
    PropertyValidator.registerProperties(SliderNumberFieldElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "Max", "Min", "MouseWheelScrollBehaviour", "NumberFieldContainerAnchor", "NumberFieldDefaultValue", "NumberFieldMaxDecimalPlaces", "NumberFieldStyle", "NumberFieldSuffix", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "SliderStyle", "Step", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // Sprite element
    elementTagMap["sprite"] = SpriteElement::class.java
    PropertyValidator.registerProperties(SpriteElement::class.java, listOf("Anchor", "Angle", "AutoPlay", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "Frame", "FramesPerSecond", "HitTestVisible", "IsPlaying", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "RepeatCount", "TextTooltipShowDelay", "TextTooltipStyle", "TexturePath", "TooltipText", "TooltipTextSpans", "Visible"))

    // TabButton element
    elementTagMap["tabbutton"] = TabButtonElement::class.java
    PropertyValidator.registerProperties(TabButtonElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "Icon", "IconAnchor", "IconSelected", "Id", "KeepScrollPosition", "LayoutMode", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "Text", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // TabNavigation element
    elementTagMap["tabnavigation"] = TabNavigationElement::class.java
    PropertyValidator.registerProperties(TabNavigationElement::class.java, listOf("AllowUnselection", "Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "SelectedTab", "Style", "Tabs", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // TextButton element
    elementTagMap["textbutton"] = TextButtonElement::class.java
    PropertyValidator.registerProperties(TextButtonElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "Text", "TextSpans", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // TextField element
    elementTagMap["textfield"] = TextFieldElement::class.java
    PropertyValidator.registerProperties(TextFieldElement::class.java, listOf("Anchor", "AutoFocus", "AutoScrollDown", "AutoSelectAll", "Background", "ContentHeight", "ContentWidth", "Decoration", "FlexWeight", "HitTestVisible", "IsReadOnly", "KeepScrollPosition", "MaskTexturePath", "MaxLength", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "PasswordChar", "PlaceholderStyle", "PlaceholderText", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Value", "Visible"))

    // TimerLabel element
    elementTagMap["timerlabel"] = TimerLabelElement::class.java
    PropertyValidator.registerProperties(TimerLabelElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "ContentHeight", "ContentWidth", "Direction", "FlexWeight", "HitTestVisible", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Paused", "Seconds", "Style", "Text", "TextSpans", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

    // ToggleButton element
    elementTagMap["togglebutton"] = ToggleButtonElement::class.java
    PropertyValidator.registerProperties(ToggleButtonElement::class.java, listOf("Anchor", "AutoScrollDown", "Background", "CheckedStyle", "ContentHeight", "ContentWidth", "Disabled", "FlexWeight", "HitTestVisible", "IsChecked", "KeepScrollPosition", "MaskTexturePath", "MouseWheelScrollBehaviour", "OutlineColor", "OutlineSize", "Overscroll", "Padding", "Style", "TextTooltipShowDelay", "TextTooltipStyle", "TooltipText", "TooltipTextSpans", "Visible"))

}

class ActionButtonElement() : Element("ActionButton") {}

class AssetImageElement() : ElementContainer("AssetImage") {}

class BackButtonElement() : Element("BackButton") {}

class BlockSelectorElement() : Element("BlockSelector") {}

class ButtonElement() : ElementContainer("Button") {}

class CharacterPreviewComponentElement() : ElementContainer("CharacterPreviewComponent") {}

class CheckBoxElement() : Element("CheckBox") {}

class CheckBoxContainerElement() : ElementContainer("CheckBoxContainer") {}

class CircularProgressBarElement() : Element("CircularProgressBar") {}

class CodeEditorElement() : Element("CodeEditor") {}

class ColorOptionGridElement() : Element("ColorOptionGrid") {}

class ColorPickerDropdownBoxElement() : Element("ColorPickerDropdownBox") {}

class CompactTextFieldElement() : Element("CompactTextField") {}

class DropdownBoxElement() : ElementContainer("DropdownBox") {}

class DropdownEntryElement() : Element("DropdownEntry") {}

class DynamicPaneElement() : ElementContainer("DynamicPane") {}

class DynamicPaneContainerElement() : ElementContainer("DynamicPaneContainer") {}

class FloatSliderElement() : Element("FloatSlider") {}

class FloatSliderNumberFieldElement() : Element("FloatSliderNumberField") {}

class GroupElement() : ElementContainer("Group") {}

class HotkeyLabelElement() : Element("HotkeyLabel") {}

class ItemGridElement() : Element("ItemGrid") {}

class ItemIconElement() : Element("ItemIcon") {}

class ItemPreviewComponentElement() : ElementContainer("ItemPreviewComponent") {}

class ItemSlotElement() : Element("ItemSlot") {}

class ItemSlotButtonElement() : ElementContainer("ItemSlotButton") {}

class LabelElement() : Element("Label") {}

class LabeledCheckBoxElement() : Element("LabeledCheckBox") {}

class MenuItemElement() : ElementContainer("MenuItem") {}

class MultilineTextFieldElement() : Element("MultilineTextField") {}

class NumberFieldElement() : Element("NumberField") {}

class PanelElement() : ElementContainer("Panel") {}

class ProgressBarElement() : Element("ProgressBar") {}

class ReorderableListElement() : ElementContainer("ReorderableList") {}

class ReorderableListGripElement() : ElementContainer("ReorderableListGrip") {}

class SceneBlurElement() : Element("SceneBlur") {}

class SliderElement() : Element("Slider") {}

class SliderNumberFieldElement() : Element("SliderNumberField") {}

class SpriteElement() : Element("Sprite") {}

class TabButtonElement() : Element("TabButton") {}

class TabNavigationElement() : ElementContainer("TabNavigation") {}

class TextButtonElement() : Element("TextButton") {}

class TextFieldElement() : Element("TextField") {}

class TimerLabelElement() : Element("TimerLabel") {}

class ToggleButtonElement() : ElementContainer("ToggleButton") {}

package li.kelp.vuetale.validator

var propertySchema: Schema? = null

fun initializeSchemas() {
    propertySchema = buildSchema {
        enum("ActionButtonAlignment", listOf("Left", "Right"))
        type(
            "Anchor", listOf(
                SchemaField("Bottom", PropertyType.Number),
                SchemaField("Full", PropertyType.Number),
                SchemaField("Height", PropertyType.Number),
                SchemaField("Horizontal", PropertyType.Number),
                SchemaField("Left", PropertyType.Number),
                SchemaField("MaxWidth", PropertyType.Number),
                SchemaField("MinWidth", PropertyType.Number),
                SchemaField("Right", PropertyType.Number),
                SchemaField("Top", PropertyType.Number),
                SchemaField("Vertical", PropertyType.Number),
                SchemaField("Width", PropertyType.Number),
            )
        )
        type(
            "BlockSelectorStyle", listOf(
                SchemaField("ItemGridStyle", PropertyType.Ref, "ItemGridStyle"),
                SchemaField("SlotDeleteIcon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SlotDropIcon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SlotHoverOverlay", PropertyType.RefOrString, "PatchStyle"),
            )
        )
        type(
            "ButtonSounds", listOf(
                SchemaField("Activate", PropertyType.Ref, "SoundStyle"),
                SchemaField("Context", PropertyType.Ref, "SoundStyle"),
                SchemaField("MouseHover", PropertyType.Ref, "SoundStyle"),
            )
        )
        type(
            "ButtonStyle", listOf(
                SchemaField("Default", PropertyType.Ref, "ButtonStyleState"),
                SchemaField("Disabled", PropertyType.Ref, "ButtonStyleState"),
                SchemaField("Hovered", PropertyType.Ref, "ButtonStyleState"),
                SchemaField("Pressed", PropertyType.Ref, "ButtonStyleState"),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "ButtonStyleState", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
            )
        )
        type(
            "CheckBoxStyle", listOf(
                SchemaField("Checked", PropertyType.Ref, "CheckBoxStyleState"),
                SchemaField("Unchecked", PropertyType.Ref, "CheckBoxStyleState"),
            )
        )
        type(
            "CheckBoxStyleState", listOf(
                SchemaField("ChangedSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("DefaultBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DisabledBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HoveredBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HoveredSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("PressedBackground", PropertyType.RefOrString, "PatchStyle"),
            )
        )
        type(
            "ClientItemStack", listOf(
                SchemaField("Durability", PropertyType.Number),
                SchemaField("Id", PropertyType.String),
                SchemaField("MaxDurability", PropertyType.Number),
                SchemaField("Metadata", PropertyType.Record),
                SchemaField("OverrideDroppedItemAnimation", PropertyType.Boolean),
                SchemaField("Quantity", PropertyType.Number),
            )
        )
        enum("CodeEditorLanguage", listOf("Json", "Text"))
        enum("ColorFormat", listOf("Rgb", "Rgba", "RgbShort"))
        type(
            "ColorOptionGridStyle", listOf(
                SchemaField("FrameBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HighlightBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HighlightOffsetLeft", PropertyType.Number),
                SchemaField("HighlightOffsetTop", PropertyType.Number),
                SchemaField("HighlightSize", PropertyType.Number),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("OptionSize", PropertyType.Number),
                SchemaField("OptionSpacingHorizontal", PropertyType.Number),
                SchemaField("OptionSpacingVertical", PropertyType.Number),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "ColorPickerDropdownBoxStateBackground", listOf(
                SchemaField("Default", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Hovered", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Pressed", PropertyType.RefOrString, "PatchStyle"),
            )
        )
        type(
            "ColorPickerDropdownBoxStyle", listOf(
                SchemaField("ArrowAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("ArrowBackground", PropertyType.Ref, "ColorPickerDropdownBoxStateBackground"),
                SchemaField("Background", PropertyType.Ref, "ColorPickerDropdownBoxStateBackground"),
                SchemaField("ColorPickerStyle", PropertyType.Ref, "ColorPickerStyle"),
                SchemaField("Overlay", PropertyType.Ref, "ColorPickerDropdownBoxStateBackground"),
                SchemaField("PanelBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("PanelHeight", PropertyType.Number),
                SchemaField("PanelOffset", PropertyType.Number),
                SchemaField("PanelPadding", PropertyType.Ref, "Padding"),
                SchemaField("PanelWidth", PropertyType.Number),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "ColorPickerStyle", listOf(
                SchemaField("ButtonBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ButtonFill", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("OpacitySelectorBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("TextFieldDecoration", PropertyType.Ref, "InputFieldDecorationStyle"),
                SchemaField("TextFieldHeight", PropertyType.Number),
                SchemaField("TextFieldInputStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("TextFieldPadding", PropertyType.Ref, "Padding"),
            )
        )
        enum("DropdownBoxAlign", listOf("Bottom", "Left", "Right", "Top"))
        type(
            "DropdownBoxSearchInputStyle", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ClearButtonStyle", PropertyType.Ref, "InputFieldButtonStyle"),
                SchemaField("Icon", PropertyType.Ref, "InputFieldIcon"),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PlaceholderStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("PlaceholderText", PropertyType.String),
                SchemaField("Style", PropertyType.Ref, "InputFieldStyle"),
            )
        )
        type(
            "DropdownBoxSounds", listOf(
                SchemaField("Activate", PropertyType.Ref, "SoundStyle"),
                SchemaField("Close", PropertyType.Ref, "SoundStyle"),
                SchemaField("MouseHover", PropertyType.Ref, "SoundStyle"),
            )
        )
        type(
            "DropdownBoxStyle", listOf(
                SchemaField("ArrowHeight", PropertyType.Number),
                SchemaField("ArrowWidth", PropertyType.Number),
                SchemaField("DefaultArrowTexturePath", PropertyType.String),
                SchemaField("DefaultBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DisabledArrowTexturePath", PropertyType.String),
                SchemaField("DisabledBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DisabledLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("EntriesInViewport", PropertyType.Number),
                SchemaField("EntryHeight", PropertyType.Number),
                SchemaField("EntryIconBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("EntryIconHeight", PropertyType.Number),
                SchemaField("EntryIconWidth", PropertyType.Number),
                SchemaField("EntryLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("EntrySounds", PropertyType.Ref, "ButtonSounds"),
                SchemaField("FocusOutlineColor", PropertyType.ColorString),
                SchemaField("FocusOutlineSize", PropertyType.Number),
                SchemaField("HorizontalEntryPadding", PropertyType.Number),
                SchemaField("HorizontalPadding", PropertyType.Number),
                SchemaField("HoveredArrowTexturePath", PropertyType.String),
                SchemaField("HoveredBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HoveredEntryBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("IconHeight", PropertyType.Number),
                SchemaField("IconTexturePath", PropertyType.String),
                SchemaField("IconWidth", PropertyType.Number),
                SchemaField("LabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("NoItemsLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("PanelAlign", PropertyType.Ref, "DropdownBoxAlign"),
                SchemaField("PanelBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("PanelOffset", PropertyType.Number),
                SchemaField("PanelPadding", PropertyType.Number),
                SchemaField("PanelScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("PanelTitleLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("PanelWidth", PropertyType.Number),
                SchemaField("PressedArrowTexturePath", PropertyType.String),
                SchemaField("PressedBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("PressedEntryBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SearchInputStyle", PropertyType.Ref, "DropdownBoxSearchInputStyle"),
                SchemaField("SelectedEntryIconBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SelectedEntryLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("Sounds", PropertyType.Ref, "DropdownBoxSounds"),
            )
        )
        enum("InputFieldButtonSide", listOf("Left", "Right"))
        type(
            "InputFieldButtonStyle", listOf(
                SchemaField("Height", PropertyType.Number),
                SchemaField("HoveredTexture", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Offset", PropertyType.Number),
                SchemaField("PressedTexture", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Side", PropertyType.Ref, "InputFieldButtonSide"),
                SchemaField("Texture", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Width", PropertyType.Number),
            )
        )
        type(
            "InputFieldDecorationStyle", listOf(
                SchemaField("Default", PropertyType.Ref, "InputFieldDecorationStyleState"),
                SchemaField("Focused", PropertyType.Ref, "InputFieldDecorationStyleState"),
            )
        )
        type(
            "InputFieldDecorationStyleState", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ClearButtonStyle", PropertyType.Ref, "InputFieldButtonStyle"),
                SchemaField("Icon", PropertyType.Ref, "InputFieldIcon"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("ToggleVisibilityButtonStyle", PropertyType.Ref, "InputFieldButtonStyle"),
            )
        )
        type(
            "InputFieldIcon", listOf(
                SchemaField("Height", PropertyType.Number),
                SchemaField("Offset", PropertyType.Number),
                SchemaField("Side", PropertyType.Ref, "InputFieldIconSide"),
                SchemaField("Texture", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Width", PropertyType.Number),
            )
        )
        enum("InputFieldIconSide", listOf("Left", "Right"))
        type(
            "InputFieldStyle", listOf(
                SchemaField("FontName", PropertyType.String),
                SchemaField("FontSize", PropertyType.Number),
                SchemaField("RenderBold", PropertyType.Boolean),
                SchemaField("RenderItalics", PropertyType.Boolean),
                SchemaField("RenderUppercase", PropertyType.Boolean),
                SchemaField("TextColor", PropertyType.ColorString),
            )
        )
        enum("ItemGridInfoDisplayMode", listOf("Adjacent", "None", "Tooltip"))
        type(
            "ItemGridSlot", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Description", PropertyType.String),
                SchemaField("ExtraOverlays", PropertyType.RefArray, "PatchStyle"),
                SchemaField("Icon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("InventorySlotIndex", PropertyType.Number),
                SchemaField("IsActivatable", PropertyType.Boolean),
                SchemaField("IsItemIncompatible", PropertyType.Boolean),
                SchemaField("IsItemUncraftable", PropertyType.Boolean),
                SchemaField("ItemStack", PropertyType.Ref, "ClientItemStack"),
                SchemaField("Name", PropertyType.String),
                SchemaField("Overlay", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SkipItemQualityBackground", PropertyType.Boolean),
            )
        )
        type(
            "ItemGridStyle", listOf(
                SchemaField("BrokenSlotBackgroundOverlay", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("BrokenSlotIconOverlay", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("CursedIconAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("CursedIconPatch", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DefaultItemIcon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DurabilityBar", PropertyType.String),
                SchemaField("DurabilityBarAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("DurabilityBarBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DurabilityBarColorEnd", PropertyType.ColorString),
                SchemaField("DurabilityBarColorStart", PropertyType.ColorString),
                SchemaField("ItemStackActivateSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("ItemStackHoveredSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("QuantityPopupSlotOverlay", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SlotBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("SlotIconSize", PropertyType.Number),
                SchemaField("SlotSize", PropertyType.Number),
                SchemaField("SlotSpacing", PropertyType.Number),
            )
        )
        enum("LabelAlignment", listOf("Center", "End", "Start"))
        type(
            "LabeledCheckBoxStyle", listOf(
                SchemaField("Checked", PropertyType.Ref, "LabeledCheckBoxStyleState"),
                SchemaField("Unchecked", PropertyType.Ref, "LabeledCheckBoxStyleState"),
            )
        )
        type(
            "LabeledCheckBoxStyleState", listOf(
                SchemaField("ChangedSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("DefaultBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DefaultLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("DisabledBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DisabledLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("HoveredBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HoveredLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("HoveredSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("PressedBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("PressedLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("Text", PropertyType.String),
            )
        )
        type(
            "LabelSpan", listOf(
                SchemaField("Color", PropertyType.ColorString),
                SchemaField("IsBold", PropertyType.Boolean),
                SchemaField("IsItalics", PropertyType.Boolean),
                SchemaField("IsMonospace", PropertyType.Boolean),
                SchemaField("IsUnderlined", PropertyType.Boolean),
                SchemaField("IsUppercase", PropertyType.Boolean),
                SchemaField("Link", PropertyType.String),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("Params", PropertyType.Record),
                SchemaField("Text", PropertyType.String),
            )
        )
        type(
            "LabelStyle", listOf(
                SchemaField("Alignment", PropertyType.Ref, "LabelAlignment"),
                SchemaField("FontName", PropertyType.String),
                SchemaField("FontSize", PropertyType.Number),
                SchemaField("HorizontalAlignment", PropertyType.Ref, "LabelAlignment"),
                SchemaField("LetterSpacing", PropertyType.Number),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("RenderBold", PropertyType.Boolean),
                SchemaField("RenderItalics", PropertyType.Boolean),
                SchemaField("RenderUnderlined", PropertyType.Boolean),
                SchemaField("RenderUppercase", PropertyType.Boolean),
                SchemaField("TextColor", PropertyType.ColorString),
                SchemaField("VerticalAlignment", PropertyType.Ref, "LabelAlignment"),
                SchemaField("Wrap", PropertyType.Boolean),
            )
        )
        enum(
            "LayoutMode",
            listOf(
                "Bottom",
                "BottomScrolling",
                "Center",
                "CenterMiddle",
                "Full",
                "Left",
                "LeftCenterWrap",
                "LeftScrolling",
                "Middle",
                "MiddleCenter",
                "Right",
                "RightScrolling",
                "Top",
                "TopScrolling"
            )
        )
        enum("MouseWheelScrollBehaviourType", listOf("Default", "HorizontalOnly", "VerticalOnly"))
        type(
            "NumberFieldFormat", listOf(
                SchemaField("DefaultValue", PropertyType.Number),
                SchemaField("MaxDecimalPlaces", PropertyType.Number),
                SchemaField("MaxValue", PropertyType.Number),
                SchemaField("MinValue", PropertyType.Number),
                SchemaField("Step", PropertyType.Number),
                SchemaField("Suffix", PropertyType.String),
            )
        )
        type(
            "Padding", listOf(
                SchemaField("Bottom", PropertyType.Number),
                SchemaField("Full", PropertyType.Number),
                SchemaField("Horizontal", PropertyType.Number),
                SchemaField("Left", PropertyType.Number),
                SchemaField("Right", PropertyType.Number),
                SchemaField("Top", PropertyType.Number),
                SchemaField("Vertical", PropertyType.Number),
            )
        )
        type(
            "PatchStyle", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("Area", PropertyType.Ref, "Padding"),
                SchemaField("Border", PropertyType.Number),
                SchemaField("Color", PropertyType.ColorString),
                SchemaField("HorizontalBorder", PropertyType.Number),
                SchemaField("TexturePath", PropertyType.String),
                SchemaField("VerticalBorder", PropertyType.Number),
            )
        )
        type(
            "PopupStyle", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ButtonPadding", PropertyType.Ref, "Padding"),
                SchemaField("ButtonStyle", PropertyType.Ref, "SubMenuItemStyle"),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("SelectedButtonStyle", PropertyType.Ref, "SubMenuItemStyle"),
                SchemaField("TooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("Width", PropertyType.Number),
            )
        )
        enum("ProgressBarAlignment", listOf("Horizontal", "Vertical"))
        enum("ProgressBarDirection", listOf("End", "Start"))
        enum("ResizeType", listOf("End", "None", "Start"))
        type(
            "ScrollbarStyle", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("DraggedHandle", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Handle", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HoveredHandle", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("OnlyVisibleWhenHovered", PropertyType.Boolean),
                SchemaField("Size", PropertyType.Number),
                SchemaField("Spacing", PropertyType.Number),
            )
        )
        type(
            "SliderStyle", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Fill", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Handle", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("HandleHeight", PropertyType.Number),
                SchemaField("HandleWidth", PropertyType.Number),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "SoundStyle", listOf(
                SchemaField("MaxPitch", PropertyType.Number),
                SchemaField("MinPitch", PropertyType.Number),
                SchemaField("SoundPath", PropertyType.String),
                SchemaField("StopExistingPlayback", PropertyType.Boolean),
                SchemaField("Volume", PropertyType.Number),
            )
        )
        type(
            "SpriteFrame", listOf(
                SchemaField("Count", PropertyType.Number),
                SchemaField("Height", PropertyType.Number),
                SchemaField("PerRow", PropertyType.Number),
                SchemaField("Width", PropertyType.Number),
            )
        )
        type(
            "SubMenuItemStyle", listOf(
                SchemaField("Default", PropertyType.Ref, "SubMenuItemStyleState"),
                SchemaField("Disabled", PropertyType.Ref, "SubMenuItemStyleState"),
                SchemaField("Hovered", PropertyType.Ref, "SubMenuItemStyleState"),
                SchemaField("Pressed", PropertyType.Ref, "SubMenuItemStyleState"),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "SubMenuItemStyleState", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("BindingLabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("LabelMaskTexturePath", PropertyType.String),
                SchemaField("LabelStyle", PropertyType.Ref, "LabelStyle"),
            )
        )
        type(
            "Tab", listOf(
                SchemaField("Icon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("IconAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("IconSelected", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Id", PropertyType.String),
                SchemaField("Text", PropertyType.String),
                SchemaField("TooltipText", PropertyType.String),
            )
        )
        type(
            "TabNavigationStyle", listOf(
                SchemaField("SelectedTabStyle", PropertyType.Ref, "TabStyle"),
                SchemaField("SeparatorAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("SeparatorBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("TabSounds", PropertyType.Ref, "ButtonSounds"),
                SchemaField("TabStyle", PropertyType.Ref, "TabStyle"),
            )
        )
        type(
            "TabStyle", listOf(
                SchemaField("Default", PropertyType.Ref, "TabStyleState"),
                SchemaField("Hovered", PropertyType.Ref, "TabStyleState"),
                SchemaField("Pressed", PropertyType.Ref, "TabStyleState"),
            )
        )
        type(
            "TabStyleState", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentMaskTexturePath", PropertyType.String),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("IconAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("IconOpacity", PropertyType.Number),
                SchemaField("LabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("Overlay", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
            )
        )
        type(
            "TextButtonStyle", listOf(
                SchemaField("Default", PropertyType.Ref, "TextButtonStyleState"),
                SchemaField("Disabled", PropertyType.Ref, "TextButtonStyleState"),
                SchemaField("Hovered", PropertyType.Ref, "TextButtonStyleState"),
                SchemaField("Pressed", PropertyType.Ref, "TextButtonStyleState"),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "TextButtonStyleState", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("LabelMaskTexturePath", PropertyType.String),
                SchemaField("LabelStyle", PropertyType.Ref, "LabelStyle"),
            )
        )
        type(
            "TextTooltipStyle", listOf(
                SchemaField("Alignment", PropertyType.Ref, "TooltipAlignment"),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("LabelStyle", PropertyType.Ref, "LabelStyle"),
                SchemaField("MaxWidth", PropertyType.Number),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
            )
        )
        enum("TimerDirection", listOf("CountDown", "CountUp"))
        type(
            "ToggleButtonStyle", listOf(
                SchemaField("Default", PropertyType.Ref, "ToggleButtonStyleState"),
                SchemaField("Disabled", PropertyType.Ref, "ToggleButtonStyleState"),
                SchemaField("Hovered", PropertyType.Ref, "ToggleButtonStyleState"),
                SchemaField("Pressed", PropertyType.Ref, "ToggleButtonStyleState"),
                SchemaField("Sounds", PropertyType.Ref, "ButtonSounds"),
            )
        )
        type(
            "ToggleButtonStyleState", listOf(
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
            )
        )
        enum("TooltipAlignment", listOf("BottomLeft", "BottomRight", "TopLeft", "TopRight"))
        element(
            "ActionButton", listOf(
                SchemaField("ActionName", PropertyType.String),
                SchemaField("Alignment", PropertyType.Ref, "ActionButtonAlignment"),
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("BindingModifier1Label", PropertyType.String),
                SchemaField("BindingModifier2Label", PropertyType.String),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsAvailable", PropertyType.Boolean),
                SchemaField("IsHoldBinding", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("KeyBindingLabel", PropertyType.String),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "ButtonStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "AssetImage", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AssetPath", PropertyType.String),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "BackButton", listOf(
                SchemaField("ActionName", PropertyType.String),
                SchemaField("Alignment", PropertyType.Ref, "ActionButtonAlignment"),
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("BindingModifier1Label", PropertyType.String),
                SchemaField("BindingModifier2Label", PropertyType.String),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsAvailable", PropertyType.Boolean),
                SchemaField("IsHoldBinding", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("KeyBindingLabel", PropertyType.String),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "ButtonStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "BlockSelector", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Capacity", PropertyType.Number),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "BlockSelectorStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "Button", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "ButtonStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "CharacterPreviewComponent", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "CheckBox", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "CheckBoxStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Boolean),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "CheckBoxContainer", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "CircularProgressBar", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Color", PropertyType.ColorString),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "CodeEditor", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoFocus", PropertyType.Boolean),
                SchemaField("AutoGrow", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("AutoSelectAll", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentPadding", PropertyType.Ref, "Padding"),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Decoration", PropertyType.Ref, "InputFieldDecorationStyle"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("Language", PropertyType.Ref, "CodeEditorLanguage"),
                SchemaField("LineNumberBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("LineNumberPadding", PropertyType.Number),
                SchemaField("LineNumberTextColor", PropertyType.ColorString),
                SchemaField("LineNumberWidth", PropertyType.Number),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MaxLength", PropertyType.Number),
                SchemaField("MaxLines", PropertyType.Number),
                SchemaField("MaxVisibleLines", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PlaceholderStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("PlaceholderText", PropertyType.String),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("Style", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ColorOptionGrid", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ColorOptions", PropertyType.Array, "String"),
                SchemaField("ColorsPerRow", PropertyType.Number),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Selected", PropertyType.String),
                SchemaField("Style", PropertyType.Ref, "ColorOptionGridStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ColorPickerDropdownBox", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Color", PropertyType.ColorString),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("DisplayTextField", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("Format", PropertyType.Ref, "ColorFormat"),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ResetTransparencyWhenChangingColor", PropertyType.Boolean),
                SchemaField("Style", PropertyType.Ref, "ColorPickerDropdownBoxStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "CompactTextField", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoFocus", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("AutoSelectAll", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("CollapsedWidth", PropertyType.Number),
                SchemaField("CollapseSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Decoration", PropertyType.Ref, "InputFieldDecorationStyle"),
                SchemaField("ExpandedWidth", PropertyType.Number),
                SchemaField("ExpandSound", PropertyType.Ref, "SoundStyle"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MaxLength", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PasswordChar", PropertyType.String),
                SchemaField("PlaceholderStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("PlaceholderText", PropertyType.String),
                SchemaField("Style", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "DropdownBox", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("DisplayNonExistingValue", PropertyType.Boolean),
                SchemaField("Entries", PropertyType.Record),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("ForcedLabel", PropertyType.String),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MaxSelection", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("NoItemsText", PropertyType.String),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PanelTitleText", PropertyType.String),
                SchemaField("SelectedValues", PropertyType.Array, "String"),
                SchemaField("ShowLabel", PropertyType.Boolean),
                SchemaField("ShowSearchInput", PropertyType.Boolean),
                SchemaField("Style", PropertyType.Ref, "DropdownBoxStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "DropdownEntry", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Selected", PropertyType.Boolean),
                SchemaField("Style", PropertyType.Ref, "ButtonStyle"),
                SchemaField("Text", PropertyType.String),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "DynamicPane", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MinSize", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ResizeAt", PropertyType.Ref, "ResizeType"),
                SchemaField("ResizerBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ResizerSize", PropertyType.Number),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "DynamicPaneContainer", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "FloatSlider", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("Max", PropertyType.Number),
                SchemaField("Min", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Step", PropertyType.Number),
                SchemaField("Style", PropertyType.Ref, "SliderStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "FloatSliderNumberField", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("Max", PropertyType.Number),
                SchemaField("Min", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("NumberFieldContainerAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("NumberFieldDefaultValue", PropertyType.Number),
                SchemaField("NumberFieldMaxDecimalPlaces", PropertyType.Number),
                SchemaField("NumberFieldStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("NumberFieldSuffix", PropertyType.String),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("SliderStyle", PropertyType.Ref, "SliderStyle"),
                SchemaField("Step", PropertyType.Number),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "Group", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "HotkeyLabel", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("InputBindingKey", PropertyType.String),
                SchemaField("InputBindingKeyPrefix", PropertyType.String),
                SchemaField("InputBindingKeyPrefixBinding", PropertyType.String),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ItemGrid", listOf(
                SchemaField("AdjacentInfoPaneGridWidth", PropertyType.Number),
                SchemaField("AllowMaxStackDraggableItems", PropertyType.Boolean),
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AreItemsDraggable", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("DisplayItemQuantity", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("InfoDisplay", PropertyType.Ref, "ItemGridInfoDisplayMode"),
                SchemaField("InventorySectionId", PropertyType.Number),
                SchemaField("ItemStacks", PropertyType.RefArray, "ClientItemStack"),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("RenderItemQualityBackground", PropertyType.Boolean),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("ShowScrollbar", PropertyType.Boolean),
                SchemaField("Slots", PropertyType.RefArray, "ItemGridSlot"),
                SchemaField("SlotsPerRow", PropertyType.Number),
                SchemaField("Style", PropertyType.Ref, "ItemGridStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ItemIcon", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("ItemId", PropertyType.String),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ShowItemTooltip", PropertyType.Boolean),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ItemPreviewComponent", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("ItemId", PropertyType.String),
                SchemaField("ItemScale", PropertyType.Number),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ItemSlot", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("ItemId", PropertyType.String),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Quantity", PropertyType.Number),
                SchemaField("ShowDurabilityBar", PropertyType.Boolean),
                SchemaField("ShowQualityBackground", PropertyType.Boolean),
                SchemaField("ShowQuantity", PropertyType.Boolean),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ItemSlotButton", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "ButtonStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "Label", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "LabelStyle"),
                SchemaField("Text", PropertyType.String),
                SchemaField("TextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "LabeledCheckBox", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "LabeledCheckBoxStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Boolean),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "MenuItem", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("Icon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("IconAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("IsSelected", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PopupStyle", PropertyType.Ref, "PopupStyle"),
                SchemaField("SelectedStyle", PropertyType.Ref, "TextButtonStyle"),
                SchemaField("Style", PropertyType.Ref, "TextButtonStyle"),
                SchemaField("Text", PropertyType.String),
                SchemaField("TextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "MultilineTextField", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoFocus", PropertyType.Boolean),
                SchemaField("AutoGrow", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("AutoSelectAll", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentPadding", PropertyType.Ref, "Padding"),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Decoration", PropertyType.Ref, "InputFieldDecorationStyle"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MaxLength", PropertyType.Number),
                SchemaField("MaxLines", PropertyType.Number),
                SchemaField("MaxVisibleLines", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PlaceholderStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("PlaceholderText", PropertyType.String),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("Style", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "NumberField", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoFocus", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("AutoSelectAll", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Decoration", PropertyType.Ref, "InputFieldDecorationStyle"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("Format", PropertyType.Ref, "NumberFieldFormat"),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MaxLength", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PasswordChar", PropertyType.String),
                SchemaField("PlaceholderStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("Style", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "Panel", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ProgressBar", listOf(
                SchemaField("Alignment", PropertyType.Ref, "ProgressBarAlignment"),
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Bar", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("BarTexturePath", PropertyType.String),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Direction", PropertyType.Ref, "ProgressBarDirection"),
                SchemaField("EffectHeight", PropertyType.Number),
                SchemaField("EffectOffset", PropertyType.Number),
                SchemaField("EffectTexturePath", PropertyType.String),
                SchemaField("EffectWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ReorderableList", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("DropIndicatorAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("DropIndicatorBackground", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ReorderableListGrip", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsDragEnabled", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("ScrollbarStyle", PropertyType.Ref, "ScrollbarStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "SceneBlur", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "Slider", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("Max", PropertyType.Number),
                SchemaField("Min", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Step", PropertyType.Number),
                SchemaField("Style", PropertyType.Ref, "SliderStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "SliderNumberField", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("Max", PropertyType.Number),
                SchemaField("Min", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("NumberFieldContainerAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("NumberFieldDefaultValue", PropertyType.Number),
                SchemaField("NumberFieldMaxDecimalPlaces", PropertyType.Number),
                SchemaField("NumberFieldStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("NumberFieldSuffix", PropertyType.String),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("SliderStyle", PropertyType.Ref, "SliderStyle"),
                SchemaField("Step", PropertyType.Number),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.Number),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "Sprite", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("Angle", PropertyType.Number),
                SchemaField("AutoPlay", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("Frame", PropertyType.Ref, "SpriteFrame"),
                SchemaField("FramesPerSecond", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsPlaying", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("RepeatCount", PropertyType.Number),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TexturePath", PropertyType.String),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "TabButton", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("Icon", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("IconAnchor", PropertyType.Ref, "Anchor"),
                SchemaField("IconSelected", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("Id", PropertyType.String),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("LayoutMode", PropertyType.Ref, "LayoutMode"),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "ButtonStyle"),
                SchemaField("Text", PropertyType.String),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "TabNavigation", listOf(
                SchemaField("AllowUnselection", PropertyType.Boolean),
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("SelectedTab", PropertyType.String),
                SchemaField("Style", PropertyType.Ref, "TabNavigationStyle"),
                SchemaField("Tabs", PropertyType.RefArray, "Tab"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "TextButton", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "TextButtonStyle"),
                SchemaField("Text", PropertyType.String),
                SchemaField("TextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "TextField", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoFocus", PropertyType.Boolean),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("AutoSelectAll", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Decoration", PropertyType.Ref, "InputFieldDecorationStyle"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsReadOnly", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MaxLength", PropertyType.Number),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("PasswordChar", PropertyType.String),
                SchemaField("PlaceholderStyle", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("PlaceholderText", PropertyType.String),
                SchemaField("Style", PropertyType.Ref, "InputFieldStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Value", PropertyType.String),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "TimerLabel", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Direction", PropertyType.Ref, "TimerDirection"),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Paused", PropertyType.Boolean),
                SchemaField("Seconds", PropertyType.Number),
                SchemaField("Style", PropertyType.Ref, "LabelStyle"),
                SchemaField("Text", PropertyType.String),
                SchemaField("TextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
        element(
            "ToggleButton", listOf(
                SchemaField("Anchor", PropertyType.Ref, "Anchor"),
                SchemaField("AutoScrollDown", PropertyType.Boolean),
                SchemaField("Background", PropertyType.RefOrString, "PatchStyle"),
                SchemaField("CheckedStyle", PropertyType.Ref, "ToggleButtonStyle"),
                SchemaField("ContentHeight", PropertyType.Number),
                SchemaField("ContentWidth", PropertyType.Number),
                SchemaField("Disabled", PropertyType.Boolean),
                SchemaField("FlexWeight", PropertyType.Number),
                SchemaField("HitTestVisible", PropertyType.Boolean),
                SchemaField("IsChecked", PropertyType.Boolean),
                SchemaField("KeepScrollPosition", PropertyType.Boolean),
                SchemaField("MaskTexturePath", PropertyType.String),
                SchemaField("MouseWheelScrollBehaviour", PropertyType.Ref, "MouseWheelScrollBehaviourType"),
                SchemaField("OutlineColor", PropertyType.ColorString),
                SchemaField("OutlineSize", PropertyType.Number),
                SchemaField("Overscroll", PropertyType.Boolean),
                SchemaField("Padding", PropertyType.Ref, "Padding"),
                SchemaField("Style", PropertyType.Ref, "ToggleButtonStyle"),
                SchemaField("TextTooltipShowDelay", PropertyType.Number),
                SchemaField("TextTooltipStyle", PropertyType.Ref, "TextTooltipStyle"),
                SchemaField("TooltipText", PropertyType.String),
                SchemaField("TooltipTextSpans", PropertyType.RefArray, "LabelSpan"),
                SchemaField("Visible", PropertyType.Boolean),
            )
        )
    }
}
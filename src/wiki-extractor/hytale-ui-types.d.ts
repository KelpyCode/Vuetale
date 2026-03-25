/* eslint-disable @typescript-eslint/no-explicit-any */
import type { DefineComponent } from 'vue'

// eslint-disable-next-line @typescript-eslint/no-empty-object-type
type C<T> = DefineComponent<T, {}, {}, {}, {}, {}, {}>

export type ActionButtonAlignment = "Left" | "Right";

export interface Anchor {
  Bottom?: number;
  Full?: number;
  Height?: number;
  Horizontal?: number;
  Left?: number;
  MaxWidth?: number;
  MinWidth?: number;
  Right?: number;
  Top?: number;
  Vertical?: number;
  Width?: number;
}

export interface BlockSelectorStyle {
  ItemGridStyle?: ItemGridStyle;
  SlotDeleteIcon?: PatchStyle | string;
  SlotDropIcon?: PatchStyle | string;
  SlotHoverOverlay?: PatchStyle | string;
}

export interface ButtonSounds {
  Activate?: SoundStyle;
  Context?: SoundStyle;
  MouseHover?: SoundStyle;
}

export interface ButtonStyle {
  Default?: ButtonStyleState;
  Disabled?: ButtonStyleState;
  Hovered?: ButtonStyleState;
  Pressed?: ButtonStyleState;
  Sounds?: ButtonSounds;
}

export interface ButtonStyleState {
  Background?: PatchStyle | string;
}

export interface CheckBoxStyle {
  Checked?: CheckBoxStyleState;
  Unchecked?: CheckBoxStyleState;
}

export interface CheckBoxStyleState {
  ChangedSound?: SoundStyle;
  DefaultBackground?: PatchStyle | string;
  DisabledBackground?: PatchStyle | string;
  HoveredBackground?: PatchStyle | string;
  HoveredSound?: SoundStyle;
  PressedBackground?: PatchStyle | string;
}

export interface ClientItemStack {
  Durability?: number;
  Id?: string;
  MaxDurability?: number;
  Metadata?: Record<string, any>;
  OverrideDroppedItemAnimation?: boolean;
  Quantity?: number;
}

export type CodeEditorLanguage = "Json" | "Text";

export type ColorFormat = "Rgb" | "Rgba" | "RgbShort";

export interface ColorOptionGridStyle {
  FrameBackground?: PatchStyle | string;
  HighlightBackground?: PatchStyle | string;
  HighlightOffsetLeft?: number;
  HighlightOffsetTop?: number;
  HighlightSize?: number;
  MaskTexturePath?: string;
  OptionSize?: number;
  OptionSpacingHorizontal?: number;
  OptionSpacingVertical?: number;
  Sounds?: ButtonSounds;
}

export interface ColorPickerDropdownBoxStateBackground {
  Default?: PatchStyle | string;
  Hovered?: PatchStyle | string;
  Pressed?: PatchStyle | string;
}

export interface ColorPickerDropdownBoxStyle {
  ArrowAnchor?: Anchor;
  ArrowBackground?: ColorPickerDropdownBoxStateBackground;
  Background?: ColorPickerDropdownBoxStateBackground;
  ColorPickerStyle?: ColorPickerStyle;
  Overlay?: ColorPickerDropdownBoxStateBackground;
  PanelBackground?: PatchStyle | string;
  PanelHeight?: number;
  PanelOffset?: number;
  PanelPadding?: Padding;
  PanelWidth?: number;
  Sounds?: ButtonSounds;
}

export interface ColorPickerStyle {
  ButtonBackground?: PatchStyle | string;
  ButtonFill?: PatchStyle | string;
  OpacitySelectorBackground?: PatchStyle | string;
  TextFieldDecoration?: InputFieldDecorationStyle;
  TextFieldHeight?: number;
  TextFieldInputStyle?: InputFieldStyle;
  TextFieldPadding?: Padding;
}

export type DropdownBoxAlign = "Bottom" | "Left" | "Right" | "Top";

export interface DropdownBoxSearchInputStyle {
  Anchor?: Anchor;
  Background?: PatchStyle | string;
  ClearButtonStyle?: InputFieldButtonStyle;
  Icon?: InputFieldIcon;
  Padding?: Padding;
  PlaceholderStyle?: InputFieldStyle;
  PlaceholderText?: string;
  Style?: InputFieldStyle;
}

export interface DropdownBoxSounds {
  Activate?: SoundStyle;
  Close?: SoundStyle;
  MouseHover?: SoundStyle;
}

export interface DropdownBoxStyle {
  ArrowHeight?: number;
  ArrowWidth?: number;
  DefaultArrowTexturePath?: string;
  DefaultBackground?: PatchStyle | string;
  DisabledArrowTexturePath?: string;
  DisabledBackground?: PatchStyle | string;
  DisabledLabelStyle?: LabelStyle;
  EntriesInViewport?: number;
  EntryHeight?: number;
  EntryIconBackground?: PatchStyle | string;
  EntryIconHeight?: number;
  EntryIconWidth?: number;
  EntryLabelStyle?: LabelStyle;
  EntrySounds?: ButtonSounds;
  FocusOutlineColor?: string;
  FocusOutlineSize?: number;
  HorizontalEntryPadding?: number;
  HorizontalPadding?: number;
  HoveredArrowTexturePath?: string;
  HoveredBackground?: PatchStyle | string;
  HoveredEntryBackground?: PatchStyle | string;
  IconHeight?: number;
  IconTexturePath?: string;
  IconWidth?: number;
  LabelStyle?: LabelStyle;
  NoItemsLabelStyle?: LabelStyle;
  PanelAlign?: DropdownBoxAlign;
  PanelBackground?: PatchStyle | string;
  PanelOffset?: number;
  PanelPadding?: number;
  PanelScrollbarStyle?: ScrollbarStyle;
  PanelTitleLabelStyle?: LabelStyle;
  PanelWidth?: number;
  PressedArrowTexturePath?: string;
  PressedBackground?: PatchStyle | string;
  PressedEntryBackground?: PatchStyle | string;
  SearchInputStyle?: DropdownBoxSearchInputStyle;
  SelectedEntryIconBackground?: PatchStyle | string;
  SelectedEntryLabelStyle?: LabelStyle;
  Sounds?: DropdownBoxSounds;
}

export type InputFieldButtonSide = "Left" | "Right";

export interface InputFieldButtonStyle {
  Height?: number;
  HoveredTexture?: PatchStyle | string;
  Offset?: number;
  PressedTexture?: PatchStyle | string;
  Side?: InputFieldButtonSide;
  Texture?: PatchStyle | string;
  Width?: number;
}

export interface InputFieldDecorationStyle {
  Default?: InputFieldDecorationStyleState;
  Focused?: InputFieldDecorationStyleState;
}

export interface InputFieldDecorationStyleState {
  Background?: PatchStyle | string;
  ClearButtonStyle?: InputFieldButtonStyle;
  Icon?: InputFieldIcon;
  OutlineColor?: string;
  OutlineSize?: number;
  ToggleVisibilityButtonStyle?: InputFieldButtonStyle;
}

export interface InputFieldIcon {
  Height?: number;
  Offset?: number;
  Side?: InputFieldIconSide;
  Texture?: PatchStyle | string;
  Width?: number;
}

export type InputFieldIconSide = "Left" | "Right";

export interface InputFieldStyle {
  FontName?: string;
  FontSize?: number;
  RenderBold?: boolean;
  RenderItalics?: boolean;
  RenderUppercase?: boolean;
  TextColor?: string;
}

export type ItemGridInfoDisplayMode = "Adjacent" | "None" | "Tooltip";

export interface ItemGridSlot {
  Background?: PatchStyle | string;
  Description?: string;
  ExtraOverlays?: Array<PatchStyle>;
  Icon?: PatchStyle | string;
  InventorySlotIndex?: number;
  IsActivatable?: boolean;
  IsItemIncompatible?: boolean;
  IsItemUncraftable?: boolean;
  ItemStack?: ClientItemStack;
  Name?: string;
  Overlay?: PatchStyle | string;
  SkipItemQualityBackground?: boolean;
}

export interface ItemGridStyle {
  BrokenSlotBackgroundOverlay?: PatchStyle | string;
  BrokenSlotIconOverlay?: PatchStyle | string;
  CursedIconAnchor?: Anchor;
  CursedIconPatch?: PatchStyle | string;
  DefaultItemIcon?: PatchStyle | string;
  DurabilityBar?: string;
  DurabilityBarAnchor?: Anchor;
  DurabilityBarBackground?: PatchStyle | string;
  DurabilityBarColorEnd?: string;
  DurabilityBarColorStart?: string;
  ItemStackActivateSound?: SoundStyle;
  ItemStackHoveredSound?: SoundStyle;
  QuantityPopupSlotOverlay?: PatchStyle | string;
  SlotBackground?: PatchStyle | string;
  SlotIconSize?: number;
  SlotSize?: number;
  SlotSpacing?: number;
}

export type LabelAlignment = "Center" | "End" | "Start";

export interface LabeledCheckBoxStyle {
  Checked?: LabeledCheckBoxStyleState;
  Unchecked?: LabeledCheckBoxStyleState;
}

export interface LabeledCheckBoxStyleState {
  ChangedSound?: SoundStyle;
  DefaultBackground?: PatchStyle | string;
  DefaultLabelStyle?: LabelStyle;
  DisabledBackground?: PatchStyle | string;
  DisabledLabelStyle?: LabelStyle;
  HoveredBackground?: PatchStyle | string;
  HoveredLabelStyle?: LabelStyle;
  HoveredSound?: SoundStyle;
  PressedBackground?: PatchStyle | string;
  PressedLabelStyle?: LabelStyle;
  Text?: string;
}

export interface LabelSpan {
  Color?: string;
  IsBold?: boolean;
  IsItalics?: boolean;
  IsMonospace?: boolean;
  IsUnderlined?: boolean;
  IsUppercase?: boolean;
  Link?: string;
  OutlineColor?: string;
  Params?: Record<string, any>;
  Text?: string;
}

export interface LabelStyle {
  Alignment?: LabelAlignment;
  FontName?: string;
  FontSize?: number;
  HorizontalAlignment?: LabelAlignment;
  LetterSpacing?: number;
  OutlineColor?: string;
  RenderBold?: boolean;
  RenderItalics?: boolean;
  RenderUnderlined?: boolean;
  RenderUppercase?: boolean;
  TextColor?: string;
  VerticalAlignment?: LabelAlignment;
  Wrap?: boolean;
}

export type LayoutMode = "Bottom" | "BottomScrolling" | "Center" | "CenterMiddle" | "Full" | "Left" | "LeftCenterWrap" | "LeftScrolling" | "Middle" | "MiddleCenter" | "Right" | "RightScrolling" | "Top" | "TopScrolling";

export type MouseWheelScrollBehaviourType = "Default" | "HorizontalOnly" | "VerticalOnly";

export interface NumberFieldFormat {
  DefaultValue?: number;
  MaxDecimalPlaces?: number;
  MaxValue?: number;
  MinValue?: number;
  Step?: number;
  Suffix?: string;
}

export interface Padding {
  Bottom?: number;
  Full?: number;
  Horizontal?: number;
  Left?: number;
  Right?: number;
  Top?: number;
  Vertical?: number;
}

export interface PatchStyle {
  Anchor?: Anchor;
  Area?: Padding;
  Border?: number;
  Color?: string;
  HorizontalBorder?: number;
  TexturePath?: string;
  VerticalBorder?: number;
}

export interface PopupStyle {
  Background?: PatchStyle | string;
  ButtonPadding?: Padding;
  ButtonStyle?: SubMenuItemStyle;
  Padding?: Padding;
  SelectedButtonStyle?: SubMenuItemStyle;
  TooltipStyle?: TextTooltipStyle;
  Width?: number;
}

export type ProgressBarAlignment = "Horizontal" | "Vertical";

export type ProgressBarDirection = "End" | "Start";

export type ResizeType = "End" | "None" | "Start";

export interface ScrollbarStyle {
  Background?: PatchStyle | string;
  DraggedHandle?: PatchStyle | string;
  Handle?: PatchStyle | string;
  HoveredHandle?: PatchStyle | string;
  OnlyVisibleWhenHovered?: boolean;
  Size?: number;
  Spacing?: number;
}

export interface SliderStyle {
  Background?: PatchStyle | string;
  Fill?: PatchStyle | string;
  Handle?: PatchStyle | string;
  HandleHeight?: number;
  HandleWidth?: number;
  Sounds?: ButtonSounds;
}

export interface SoundStyle {
  MaxPitch?: number;
  MinPitch?: number;
  SoundPath?: string;
  StopExistingPlayback?: boolean;
  Volume?: number;
}

export interface SpriteFrame {
  Count?: number;
  Height?: number;
  PerRow?: number;
  Width?: number;
}

export interface SubMenuItemStyle {
  Default?: SubMenuItemStyleState;
  Disabled?: SubMenuItemStyleState;
  Hovered?: SubMenuItemStyleState;
  Pressed?: SubMenuItemStyleState;
  Sounds?: ButtonSounds;
}

export interface SubMenuItemStyleState {
  Background?: PatchStyle | string;
  BindingLabelStyle?: LabelStyle;
  LabelMaskTexturePath?: string;
  LabelStyle?: LabelStyle;
}

export interface Tab {
  Icon?: PatchStyle | string;
  IconAnchor?: Anchor;
  IconSelected?: PatchStyle | string;
  Id?: string;
  Text?: string;
  TooltipText?: string;
}

export interface TabNavigationStyle {
  SelectedTabStyle?: TabStyle;
  SeparatorAnchor?: Anchor;
  SeparatorBackground?: PatchStyle | string;
  TabSounds?: ButtonSounds;
  TabStyle?: TabStyle;
}

export interface TabStyle {
  Default?: TabStyleState;
  Hovered?: TabStyleState;
  Pressed?: TabStyleState;
}

export interface TabStyleState {
  Anchor?: Anchor;
  Background?: PatchStyle | string;
  ContentMaskTexturePath?: string;
  FlexWeight?: number;
  IconAnchor?: Anchor;
  IconOpacity?: number;
  LabelStyle?: LabelStyle;
  Overlay?: PatchStyle | string;
  Padding?: Padding;
  TooltipStyle?: TextTooltipStyle;
}

export interface TextButtonStyle {
  Default?: TextButtonStyleState;
  Disabled?: TextButtonStyleState;
  Hovered?: TextButtonStyleState;
  Pressed?: TextButtonStyleState;
  Sounds?: ButtonSounds;
}

export interface TextButtonStyleState {
  Background?: PatchStyle | string;
  LabelMaskTexturePath?: string;
  LabelStyle?: LabelStyle;
}

export interface TextTooltipStyle {
  Alignment?: TooltipAlignment;
  Background?: PatchStyle | string;
  LabelStyle?: LabelStyle;
  MaxWidth?: number;
  Padding?: Padding;
}

export type TimerDirection = "CountDown" | "CountUp";

export interface ToggleButtonStyle {
  Default?: ToggleButtonStyleState;
  Disabled?: ToggleButtonStyleState;
  Hovered?: ToggleButtonStyleState;
  Pressed?: ToggleButtonStyleState;
  Sounds?: ButtonSounds;
}

export interface ToggleButtonStyleState {
  Background?: PatchStyle | string;
}

export type TooltipAlignment = "BottomLeft" | "BottomRight" | "TopLeft" | "TopRight";

declare module 'vue' {
    export interface GlobalComponents {

      ActionButton: C<{
        actionName?: string;
        alignment?: ActionButtonAlignment;
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        bindingModifier1Label?: string;
        bindingModifier2Label?: string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        isAvailable?: boolean;
        /** Whether this keybinding needs to be held down */
        isHoldBinding?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        keyBindingLabel?: string;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: ButtonStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      AssetImage: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        assetPath?: string;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      BackButton: C<{
        actionName?: string;
        alignment?: ActionButtonAlignment;
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        bindingModifier1Label?: string;
        bindingModifier2Label?: string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        isAvailable?: boolean;
        /** Whether this keybinding needs to be held down */
        isHoldBinding?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        keyBindingLabel?: string;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: ButtonStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      BlockSelector: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        capacity?: number;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        style?: BlockSelectorStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      Button: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: ButtonStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      CharacterPreviewComponent: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      CheckBox: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        style?: CheckBoxStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** The input value */
        value?: boolean;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      CheckBoxContainer: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      CircularProgressBar: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        color?: string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      CodeEditor: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** If true, the element will automatically set itself as the focused element when after being mounted. If there are other elements with this property enabled, the last of them to be mounted will gain focus. */
        autoFocus?: boolean;
        /** Whether the input show grow for new lines or have a static height based on . Always enabled if MaxLines is not specified. */
        autoGrow?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Automatically selects all text after being mounted. This property won't work unless is enabled. */
        autoSelectAll?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        contentPadding?: Padding;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Field style properties */
        decoration?: InputFieldDecorationStyle;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether the value of this field can be edited or not */
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        language?: CodeEditorLanguage;
        lineNumberBackground?: PatchStyle | string;
        lineNumberPadding?: number;
        lineNumberTextColor?: string;
        lineNumberWidth?: number;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum amount of characters that can be entered into this field */
        maxLength?: number;
        /** The maximum number of lines that can be entered in this field (0 for unlimited). */
        maxLines?: number;
        maxVisibleLines?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Text style properties for placeholder text */
        placeholderStyle?: InputFieldStyle;
        /** Text that will be displayed when the field is empty */
        placeholderText?: string;
        scrollbarStyle?: ScrollbarStyle;
        /** Text style properties */
        style?: InputFieldStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ColorOptionGrid: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        colorOptions?: Array<unknown>;
        colorsPerRow?: number;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        selected?: unknown;
        style?: ColorOptionGridStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ColorPickerDropdownBox: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        color?: string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** If set to true a text field will be displayed that is showing the current hex color, for copy and pasting */
        displayTextField?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        format?: ColorFormat;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** If set to true and the color is being changed while the transparency is set to 0, the transparency will be set to opaque */
        resetTransparencyWhenChangingColor?: boolean;
        style?: ColorPickerDropdownBoxStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      CompactTextField: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** If true, the element will automatically set itself as the focused element when after being mounted. If there is other elements with AutoFocus on, then the last element getting mounted will be the last to be focused */
        autoFocus?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Automatically selects all text after being mounted. This property won't work unless AutoFocus is turned on as well */
        autoSelectAll?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        collapsedWidth?: number;
        collapseSound?: SoundStyle;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Field style properties */
        decoration?: InputFieldDecorationStyle;
        expandedWidth?: number;
        expandSound?: SoundStyle;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether the value of this field can be edited or not */
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum amount of characters that can be entered into this field */
        maxLength?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Replaces all character occurences in this field with the specified character. Used for password fields */
        passwordChar?: string;
        /** Text style properties for placeholder text */
        placeholderStyle?: InputFieldStyle;
        /** Text that will be displayed when the field is empty */
        placeholderText?: string;
        /** Text style properties */
        style?: InputFieldStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      DropdownBox: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        disabled?: boolean;
        displayNonExistingValue?: boolean;
        entries?: Array<any>;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        forcedLabel?: string;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        maxSelection?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        noItemsText?: string;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        panelTitleText?: string;
        selectedValues?: Array<string>;
        showLabel?: boolean;
        showSearchInput?: boolean;
        style?: DropdownBoxStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      DropdownEntry: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        selected?: boolean;
        /** Button style */
        style?: ButtonStyle;
        text?: string;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      DynamicPane: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The minimum size considered when resizing panes. Not considered in layout algorithm. */
        minSize?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Position of the resizer, if at all */
        resizeAt?: ResizeType;
        /** Background of the resizer */
        resizerBackground?: PatchStyle | string;
        /** Width or height of the resizer */
        resizerSize?: number;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      DynamicPaneContainer: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      FloatSlider: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum allowed value */
        max?: number;
        /** The minimum allowed value */
        min?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** The amount by which to increment/decrement the value */
        step?: number;
        /** Style properties */
        style?: SliderStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      FloatSliderNumberField: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** Max allowed value */
        max?: number;
        /** Min allowed value */
        min?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Number field container anchor */
        numberFieldContainerAnchor?: Anchor;
        /** See */
        numberFieldDefaultValue?: number;
        /** See */
        numberFieldMaxDecimalPlaces?: number;
        /** Number field style properties */
        numberFieldStyle?: InputFieldStyle;
        numberFieldSuffix?: string;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Slider syle properties */
        sliderStyle?: SliderStyle;
        /** Amount by which to increment/decrement the value */
        step?: number;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      Group: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      HotkeyLabel: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        inputBindingKey?: string;
        inputBindingKeyPrefix?: string;
        inputBindingKeyPrefixBinding?: string;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ItemGrid: C<{
        adjacentInfoPaneGridWidth?: number;
        allowMaxStackDraggableItems?: boolean;
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        areItemsDraggable?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        displayItemQuantity?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        infoDisplay?: ItemGridInfoDisplayMode;
        inventorySectionId?: number;
        itemStacks?: ClientItemStack[];
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        renderItemQualityBackground?: boolean;
        scrollbarStyle?: ScrollbarStyle;
        showScrollbar?: boolean;
        slots?: ItemGridSlot[];
        slotsPerRow?: number;
        style?: ItemGridStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ItemIcon: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        itemId?: string;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** When true, shows the full item tooltip (with description, quality, etc.) on hover. Note: When inside an ItemSlotButton, leave this false - the parent handles tooltips. */
        showItemTooltip?: boolean;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ItemPreviewComponent: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        itemId?: string;
        itemScale?: number;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ItemSlot: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        itemId?: string;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        quantity?: number;
        showDurabilityBar?: boolean;
        showQualityBackground?: boolean;
        showQuantity?: boolean;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ItemSlotButton: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: ButtonStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      Label: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Label style */
        style?: LabelStyle;
        /** Text */
        text?: string;
        /** Text spans. Used for formatting text */
        textSpans?: Array<LabelSpan>;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      LabeledCheckBox: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        style?: LabeledCheckBoxStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** The input value */
        value?: boolean;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      MenuItem: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        icon?: PatchStyle | string;
        iconAnchor?: Anchor;
        isSelected?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        popupStyle?: PopupStyle;
        selectedStyle?: TextButtonStyle;
        /** Button style */
        style?: TextButtonStyle;
        /** Button text */
        text?: string;
        /** Text spans. Used for formatting text */
        textSpans?: Array<LabelSpan>;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      MultilineTextField: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** If true, the element will automatically set itself as the focused element when after being mounted. If there are other elements with this property enabled, the last of them to be mounted will gain focus. */
        autoFocus?: boolean;
        /** Whether the input show grow for new lines or have a static height based on . Always enabled if MaxLines is not specified. */
        autoGrow?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Automatically selects all text after being mounted. This property won't work unless is enabled. */
        autoSelectAll?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        contentPadding?: Padding;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Field style properties */
        decoration?: InputFieldDecorationStyle;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether the value of this field can be edited or not */
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum amount of characters that can be entered into this field */
        maxLength?: number;
        /** The maximum number of lines that can be entered in this field (0 for unlimited). */
        maxLines?: number;
        maxVisibleLines?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Text style properties for placeholder text */
        placeholderStyle?: InputFieldStyle;
        /** Text that will be displayed when the field is empty */
        placeholderText?: string;
        scrollbarStyle?: ScrollbarStyle;
        /** Text style properties */
        style?: InputFieldStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      NumberField: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** If true, the element will automatically set itself as the focused element when after being mounted. If there is other elements with AutoFocus on, then the last element getting mounted will be the last to be focused */
        autoFocus?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Automatically selects all text after being mounted. This property won't work unless AutoFocus is turned on as well */
        autoSelectAll?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Field style properties */
        decoration?: InputFieldDecorationStyle;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** Number value options */
        format?: NumberFieldFormat;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether the value of this field can be edited or not */
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum amount of characters that can be entered into this field */
        maxLength?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Replaces all character occurences in this field with the specified character. Used for password fields */
        passwordChar?: string;
        /** Text style properties for placeholder text */
        placeholderStyle?: InputFieldStyle;
        /** Text style properties */
        style?: InputFieldStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      Panel: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ProgressBar: C<{
        alignment?: ProgressBarAlignment;
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        bar?: PatchStyle | string;
        barTexturePath?: string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        direction?: ProgressBarDirection;
        effectHeight?: number;
        effectOffset?: number;
        effectTexturePath?: string;
        effectWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ReorderableList: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        dropIndicatorAnchor?: Anchor;
        dropIndicatorBackground?: PatchStyle | string;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ReorderableListGrip: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Allows to toggle whether this element is draggable or not */
        isDragEnabled?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        scrollbarStyle?: ScrollbarStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      SceneBlur: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      Slider: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether the value of this field can be edited or not */
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum allowed value */
        max?: number;
        /** The minimum allowed value */
        min?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** The amount by which to increment/decrement the value */
        step?: number;
        /** Style properties */
        style?: SliderStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      SliderNumberField: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** Max allowed value */
        max?: number;
        /** Min allowed value */
        min?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Number field container anchor */
        numberFieldContainerAnchor?: Anchor;
        /** See */
        numberFieldDefaultValue?: number;
        /** See */
        numberFieldMaxDecimalPlaces?: number;
        /** Number field style properties */
        numberFieldStyle?: InputFieldStyle;
        numberFieldSuffix?: string;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Slider syle properties */
        sliderStyle?: SliderStyle;
        /** Amount by which to increment/decrement the value */
        step?: number;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: number;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      Sprite: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Rotates this element by a specific angle in degrees */
        angle?: number;
        /** Whether the animation should start playing automatically as soon as this element gets mounted */
        autoPlay?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** Extra information that defines the layout of the sprite sheet texture */
        frame?: SpriteFrame;
        /** How many frames should be displayed per second. Defines the speed of the animation */
        framesPerSecond?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        isPlaying?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** How often the sprite animation should be repeated. Should be set to 0 for looping infinite times */
        repeatCount?: number;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** The spritesheet texture */
        texturePath?: string;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      TabButton: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        icon?: PatchStyle | string;
        iconAnchor?: Anchor;
        iconSelected?: PatchStyle | string;
        id?: string;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Determines how child elements will be laid out */
        layoutMode?: LayoutMode;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: ButtonStyle;
        text?: string;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      TabNavigation: C<{
        allowUnselection?: boolean;
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        selectedTab?: string;
        style?: TabNavigationStyle;
        tabs?: Tab[];
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      TextButton: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: TextButtonStyle;
        /** Button text */
        text?: string;
        /** Text spans. Used for formatting text */
        textSpans?: Array<LabelSpan>;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      TextField: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** If true, the element will automatically set itself as the focused element when after being mounted. If there is other elements with AutoFocus on, then the last element getting mounted will be the last to be focused */
        autoFocus?: boolean;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Automatically selects all text after being mounted. This property won't work unless AutoFocus is turned on as well */
        autoSelectAll?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Field style properties */
        decoration?: InputFieldDecorationStyle;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether the value of this field can be edited or not */
        isReadOnly?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        /** The maximum amount of characters that can be entered into this field */
        maxLength?: number;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Replaces all character occurences in this field with the specified character. Used for password fields */
        passwordChar?: string;
        /** Text style properties for placeholder text */
        placeholderStyle?: InputFieldStyle;
        /** Text that will be displayed when the field is empty */
        placeholderText?: string;
        /** Text style properties */
        style?: InputFieldStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        value?: string;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      TimerLabel: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        direction?: TimerDirection;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        paused?: boolean;
        seconds?: number;
        /** Label style */
        style?: LabelStyle;
        /** Text */
        text?: string;
        /** Text spans. Used for formatting text */
        textSpans?: Array<LabelSpan>;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

      ToggleButton: C<{
        /** Defines how the element should be laid out inside its allocated area */
        anchor?: Anchor;
        /** Makes the element automatically scroll down to the bottom of the element. The element won't try to scroll down if it has been scrolled up */
        autoScrollDown?: boolean;
        /** Sets a background image or color */
        background?: PatchStyle | string;
        checkedStyle?: ToggleButtonStyle;
        /** If set, this element will display a vertical scrollbar */
        contentHeight?: number;
        /** If set, this element will display a horizontal scrollbar */
        contentWidth?: number;
        /** Whether the button is clickable or not. If true mouse click related event callbacks won't be triggered anymore */
        disabled?: boolean;
        /** FlexWeight can be used to distribute any left-over space after explicit widths/heights have been subtracted */
        flexWeight?: number;
        /** By default, most elements won't return themselves during a HitTest check unless they expose certain functionality that requires them to (e.g. a button). This means that elements visually underneath them might receive the input event instead. If you want this element to be returned during a HitTest set this to true */
        hitTestVisible?: boolean;
        isChecked?: boolean;
        /** Whether to keep the scrolling position, even after the element has been unmounted */
        keepScrollPosition?: boolean;
        /** Sets a mask texture to be used for clipping. (Doesn't support 9-patches yet) */
        maskTexturePath?: string;
        mouseWheelScrollBehaviour?: MouseWheelScrollBehaviourType;
        /** Sets the color for the outline to render */
        outlineColor?: string;
        /** Draws a basic outline around the element with the specified size */
        outlineSize?: number;
        /** If enabled scrolling areas will be extended by the size of the element */
        overscroll?: boolean;
        /** Defines how much space to keep around the content. Background is unaffected. */
        padding?: Padding;
        /** Button style */
        style?: ToggleButtonStyle;
        /** Specifies a delay in seconds for long the mouse has to stay on this element for the tooltip to appear */
        textTooltipShowDelay?: number;
        /** Style options for the text tooltip */
        textTooltipStyle?: TextTooltipStyle;
        /** Enables a text tooltip and sets the text to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipText?: string;
        /** Enables a text tooltip and sets the text spans to show. The tooltip will be shown while the mouse cursor is on this element */
        tooltipTextSpans?: Array<LabelSpan>;
        /** Hides the element. Makes the parent layouting skip this element as well */
        visible?: boolean;
      }>;

   }
}
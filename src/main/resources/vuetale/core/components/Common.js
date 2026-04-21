import { defineComponent as r, h as t } from "vue";
const u = r({
  name: "Panel",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, background: { TexturePath: "Common/ContainerFullPatch.png", Border: 20 } });
  }
}), c = r({
  name: "TitleLabel",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Label", { ...n, elStyle: { FontSize: 40, Alignment: "Center" } });
  }
}), s = r({
  name: "TextButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, text: l, ...d } = e;
    return () => t("TextButton", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Primary.png", VerticalBorder: 12, HorizontalBorder: 80 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Hovered: { Background: { TexturePath: "Common/Buttons/Primary_Hovered.png", VerticalBorder: 12, HorizontalBorder: 80 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Pressed: { Background: { TexturePath: "Common/Buttons/Primary_Pressed.png", VerticalBorder: 12, HorizontalBorder: 80 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", VerticalBorder: 12, HorizontalBorder: 80 }, LabelStyle: { FontSize: 17, TextColor: "#797b7c", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Sounds: e.sounds }, anchor: e.anchor, padding: { Horizontal: 24 }, text: e.text });
  }
}), i = r({
  name: "Button",
  slots: Object,
  props: {
    defaultSquareButtonStyle: { type: null, required: !1 },
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { defaultSquareButtonStyle: n, sounds: a, anchor: l, ...d } = e;
    return () => t("Button", { ...d, elStyle: e.defaultSquareButtonStyle, anchor: e.anchor, padding: { Horizontal: 24 } });
  }
}), p = r({
  name: "CancelTextButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, text: l, ...d } = e;
    return () => t("TextButton", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Destructive.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Hovered: { Background: { TexturePath: "Common/Buttons/Destructive_Hovered.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Pressed: { Background: { TexturePath: "Common/Buttons/Destructive_Pressed.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bfcdd5", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Sounds: e.sounds }, anchor: e.anchor, padding: { Horizontal: 24 }, text: e.text });
  }
}), g = r({
  name: "CancelButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    width: {
      type: Number,
      default: 44
    }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, width: l, ...d } = e;
    return () => t("Button", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Destructive.png", Border: 12 } }, Hovered: { Background: { TexturePath: "Common/Buttons/Destructive_Hovered.png", Border: 12 } }, Pressed: { Background: { TexturePath: "Common/Buttons/Destructive_Pressed.png", Border: 12 } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 } }, Sounds: e.sounds }, anchor: e.anchor });
  }
}), m = r({
  name: "SmallSecondaryTextButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, text: l, ...d } = e;
    return () => t("TextButton", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Secondary.png", Border: 12 }, LabelStyle: { FontSize: 14, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Hovered: { Background: { TexturePath: "Common/Buttons/Secondary_Hovered.png", Border: 12 }, LabelStyle: { FontSize: 14, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Pressed: { Background: { TexturePath: "Common/Buttons/Secondary_Pressed.png", Border: 12 }, LabelStyle: { FontSize: 14, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 }, LabelStyle: { FontSize: 14, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Sounds: e.sounds }, anchor: e.anchor, padding: { Horizontal: 16 }, text: e.text });
  }
}), B = r({
  name: "SmallTertiaryTextButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, text: l, ...d } = e;
    return () => t("TextButton", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Tertiary.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Hovered: { Background: { TexturePath: "Common/Buttons/Tertiary_Hovered.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Pressed: { Background: { TexturePath: "Common/Buttons/Tertiary_Pressed.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Sounds: e.sounds }, anchor: e.anchor, padding: { Horizontal: 16 }, text: e.text });
  }
}), h = r({
  name: "SecondaryTextButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, text: l, ...d } = e;
    return () => t("TextButton", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Secondary.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Hovered: { Background: { TexturePath: "Common/Buttons/Secondary_Hovered.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Pressed: { Background: { TexturePath: "Common/Buttons/Secondary_Pressed.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Sounds: e.sounds }, anchor: e.anchor, padding: { Horizontal: 24 }, text: e.text });
  }
}), C = r({
  name: "SecondaryButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    width: {
      type: Number,
      default: 44
    }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, width: l, ...d } = e;
    return () => t("Button", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Secondary.png", Border: 12 } }, Hovered: { Background: { TexturePath: "Common/Buttons/Secondary_Hovered.png", Border: 12 } }, Pressed: { Background: { TexturePath: "Common/Buttons/Secondary_Pressed.png", Border: 12 } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 } }, Sounds: e.sounds }, anchor: e.anchor });
  }
}), S = r({
  name: "TertiaryTextButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, text: l, ...d } = e;
    return () => t("TextButton", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Tertiary.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Hovered: { Background: { TexturePath: "Common/Buttons/Tertiary_Hovered.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Pressed: { Background: { TexturePath: "Common/Buttons/Tertiary_Pressed.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 }, LabelStyle: { FontSize: 17, TextColor: "#bdcbd3", RenderBold: !0, RenderUppercase: !0, HorizontalAlignment: "Center", VerticalAlignment: "Center" } }, Sounds: e.sounds }, anchor: e.anchor, padding: { Horizontal: 24 }, text: e.text });
  }
}), b = r({
  name: "TertiaryButton",
  slots: Object,
  props: {
    sounds: {
      type: Object,
      default: () => ({})
    },
    anchor: {
      type: Object,
      default: () => ({})
    },
    width: {
      type: Number,
      default: 44
    }
  },
  setup(e, { slots: o }) {
    const { sounds: n, anchor: a, width: l, ...d } = e;
    return () => t("Button", { ...d, elStyle: { Default: { Background: { TexturePath: "Common/Buttons/Tertiary.png", Border: 12 } }, Hovered: { Background: { TexturePath: "Common/Buttons/Tertiary_Hovered.png", Border: 12 } }, Pressed: { Background: { TexturePath: "Common/Buttons/Tertiary_Pressed.png", Border: 12 } }, Disabled: { Background: { TexturePath: "Common/Buttons/Disabled.png", Border: 12 } }, Sounds: e.sounds }, anchor: e.anchor });
  }
}), x = r({
  name: "CloseButton",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Button", { ...n, anchor: { Top: -16, Right: -16, Width: 32, Height: 32 }, elStyle: { Default: { Background: "Common/ContainerCloseButton.png" }, Hovered: { Background: "Common/ContainerCloseButtonHovered.png" }, Pressed: { Background: "Common/ContainerCloseButtonPressed.png" } } });
  }
}), P = r({
  name: "CheckBox",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("CheckBox", { ...n, anchor: { Width: 22, Height: 22 }, background: { TexturePath: "Common/CheckBoxFrame.png", Border: 7 }, padding: { Full: 4 }, elStyle: { Unchecked: { DefaultBackground: { Color: "#00000000" }, HoveredBackground: { Color: "#00000000" }, PressedBackground: { Color: "#00000000" }, DisabledBackground: { Color: "#424242" }, ChangedSound: { SoundPath: "Sounds/UntickActivate.ogg", Volume: 6 } }, Checked: { DefaultBackground: { TexturePath: "Common/Checkmark.png" }, HoveredBackground: { TexturePath: "Common/Checkmark.png" }, PressedBackground: { TexturePath: "Common/Checkmark.png" }, ChangedSound: { SoundPath: "Sounds/TickActivate.ogg", Volume: 6 } } } });
  }
}), T = r({
  name: "CheckBoxWithLabel",
  slots: Object,
  props: {
    checked: {
      type: Boolean,
      default: !1
    },
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { checked: n, text: a, ...l } = e;
    return () => t("Group", { ...l, layoutMode: "Left" }, [
      t("CheckBox", { anchor: { Width: 22, Height: 22 }, background: { TexturePath: "Common/CheckBoxFrame.png", Border: 7 }, padding: { Full: 4 }, elStyle: { Unchecked: { DefaultBackground: { Color: "#00000000" }, HoveredBackground: { Color: "#00000000" }, PressedBackground: { Color: "#00000000" }, DisabledBackground: { Color: "#424242" }, ChangedSound: { SoundPath: "Sounds/UntickActivate.ogg", Volume: 6 } }, Checked: { DefaultBackground: { TexturePath: "Common/Checkmark.png" }, HoveredBackground: { TexturePath: "Common/Checkmark.png" }, PressedBackground: { TexturePath: "Common/Checkmark.png" }, ChangedSound: { SoundPath: "Sounds/TickActivate.ogg", Volume: 6 } } }, value: e.checked, id: "CheckBox" }),
      t("Label", { text: e.text, anchor: { Right: 30, Left: 11 }, elStyle: { FontSize: 16, TextColor: "#96a9be", VerticalAlignment: "Center" } })
    ]);
  }
}), y = r({
  name: "TextField",
  slots: Object,
  props: {
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { anchor: n, ...a } = e;
    return () => t("TextField", { ...a, elStyle: {}, placeholderStyle: { TextColor: "#6e7da1" }, background: { TexturePath: "Common/InputBox.png", Border: 16 }, anchor: e.anchor, padding: { Horizontal: 10 } });
  }
}), f = r({
  name: "NumberField",
  slots: Object,
  props: {
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { anchor: n, ...a } = e;
    return () => t("NumberField", { ...a, elStyle: {}, placeholderStyle: { TextColor: "#6e7da1" }, background: { TexturePath: "Common/InputBox.png", Border: 16 }, anchor: e.anchor, padding: { Horizontal: 10 } });
  }
}), H = r({
  name: "DropdownBox",
  slots: Object,
  props: {
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { anchor: n, ...a } = e;
    return () => t("DropdownBox", { ...a, anchor: e.anchor, elStyle: { DefaultBackground: { TexturePath: "Common/Dropdown.png", Border: 16 }, HoveredBackground: { TexturePath: "Common/DropdownHovered.png", Border: 16 }, PressedBackground: { TexturePath: "Common/DropdownPressed.png", Border: 16 }, DefaultArrowTexturePath: "Common/DropdownCaret.png", HoveredArrowTexturePath: "Common/DropdownCaret.png", PressedArrowTexturePath: "Common/DropdownPressedCaret.png", ArrowWidth: 13, ArrowHeight: 18, LabelStyle: { TextColor: "#96a9be", RenderUppercase: !0, VerticalAlignment: "Center", FontSize: 13 }, EntryLabelStyle: { TextColor: "#b7cedd", RenderUppercase: !0, VerticalAlignment: "Center", FontSize: 13 }, NoItemsLabelStyle: { TextColor: "#b7cedd(0.5)", RenderUppercase: !0, VerticalAlignment: "Center", FontSize: 13 }, SelectedEntryLabelStyle: { TextColor: "#b7cedd", RenderUppercase: !0, VerticalAlignment: "Center", FontSize: 13, RenderBold: !0 }, HorizontalPadding: 8, PanelScrollbarStyle: { Spacing: 6, Size: 6, Background: { TexturePath: "Common/Scrollbar.png", Border: 3 }, Handle: { TexturePath: "Common/ScrollbarHandle.png", Border: 3 }, HoveredHandle: { TexturePath: "Common/ScrollbarHandleHovered.png", Border: 3 }, DraggedHandle: { TexturePath: "Common/ScrollbarHandleDragged.png", Border: 3 } }, PanelBackground: { TexturePath: "Common/DropdownBox.png", Border: 16 }, PanelPadding: 6, PanelAlign: "Right", PanelOffset: 7, EntryHeight: 31, EntriesInViewport: 10, HorizontalEntryPadding: 7, HoveredEntryBackground: { Color: "#0a0f17" }, PressedEntryBackground: { Color: "#0f1621" }, Sounds: { Activate: { SoundPath: "Sounds/TickActivate.ogg", Volume: 6 }, MouseHover: { SoundPath: "Sounds/ButtonsLightHover.ogg", Volume: 6 }, Close: { SoundPath: "Sounds/ButtonsCancelActivate.ogg", Volume: 6 } }, EntrySounds: { Activate: { SoundPath: "Sounds/ButtonsLightActivate.ogg", MinPitch: -0.4, MaxPitch: 0.4, Volume: 4 }, MouseHover: { SoundPath: "Sounds/ButtonsLightHover.ogg", Volume: 6 } }, FocusOutlineSize: 1, FocusOutlineColor: "#ffffff(0.4)" } });
  }
}), k = r({
  name: "ContentSeparator",
  slots: Object,
  props: {
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { anchor: n, ...a } = e;
    return () => t("Group", { ...a, anchor: e.anchor, background: { Color: "#2b3542" } });
  }
}), v = r({
  name: "DefaultSpinner",
  slots: Object,
  props: {
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { anchor: n, ...a } = e;
    return () => t("Sprite", { ...a, anchor: e.anchor, texturePath: "Common/Spinner.png", frame: { Width: 32, Height: 32, PerRow: 8, Count: 72 }, framesPerSecond: 30 });
  }
}), z = r({
  name: "ActionButtonContainer",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, layoutMode: "Right", anchor: { Right: 50, Bottom: 50, Height: 27 } });
  }
}), A = r({
  name: "ActionButtonSeparator",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, anchor: { Width: 35 } });
  }
}), R = r({
  name: "VerticalActionButtonSeparator",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, anchor: { Height: 20 } });
  }
}), D = r({
  name: "Subtitle",
  slots: Object,
  props: {
    text: { type: null, required: !1 }
  },
  setup(e, { slots: o }) {
    const { text: n, ...a } = e;
    return () => t("Label", { ...a, elStyle: { FontSize: 15, RenderUppercase: !0, TextColor: "#96a9be" }, text: e.text, anchor: { Bottom: 10 } });
  }
}), O = r({
  name: "Title",
  slots: Object,
  props: {
    alignment: {
      type: String,
      default: "Center"
    },
    text: {
      type: String,
      default: ""
    }
  },
  setup(e, { slots: o }) {
    const { alignment: n, text: a, ...l } = e;
    return () => t("Label", { ...l, elStyle: { FontSize: 15, VerticalAlignment: "Center", RenderUppercase: !0, TextColor: "#b4c8c9", FontName: "Secondary", RenderBold: !0, LetterSpacing: 0, HorizontalAlignment: e.alignment }, padding: { Horizontal: 19 }, text: e.text });
  }
}), j = r({
  name: "HeaderSearch",
  slots: Object,
  props: {
    marginRight: {
      type: Number,
      default: 10
    }
  },
  setup(e, { slots: o }) {
    const { marginRight: n, ...a } = e;
    return () => t("Group", { ...a, anchor: { Width: 200, Right: 0 } }, [
      t("CompactTextField", { anchor: { Height: 30, Right: e.marginRight }, collapsedWidth: 34, expandedWidth: 200, placeholderText: "%server.customUI.searchPlaceholder", elStyle: { FontSize: 16 }, placeholderStyle: { TextColor: "#3d5a85", RenderUppercase: !0, FontSize: 14 }, padding: { Horizontal: 12, Left: 34 }, decoration: { Default: { Icon: { Texture: "Common/SearchIcon.png", Width: 16, Height: 16, Offset: 9 }, ClearButtonStyle: { Texture: { TexturePath: "Common/ClearInputIcon.png", Color: "#ffffff(0.3)" }, HoveredTexture: { TexturePath: "Common/ClearInputIcon.png", Color: "#ffffff(0.5)" }, PressedTexture: { TexturePath: "Common/ClearInputIcon.png", Color: "#ffffff(0.4)" }, Width: 16, Height: 16, Side: "Right", Offset: 10 } } }, id: "SearchInput" })
    ]);
  }
}), F = r({
  name: "HeaderTextButton",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("TextButton", { ...n, elStyle: { Default: { LabelStyle: { FontSize: 15, VerticalAlignment: "Center", RenderUppercase: !0, TextColor: "#d3d6db", FontName: "Default", RenderBold: !0, LetterSpacing: 1 } }, Hovered: { LabelStyle: { FontSize: 15, VerticalAlignment: "Center", RenderUppercase: !0, TextColor: "#eaebee", FontName: "Default", RenderBold: !0, LetterSpacing: 1 } }, Pressed: { LabelStyle: { FontSize: 15, VerticalAlignment: "Center", RenderUppercase: !0, TextColor: "#b6bbc2", FontName: "Default", RenderBold: !0, LetterSpacing: 1 } } }, padding: { Right: 22, Left: 15, Bottom: 1 } });
  }
}), V = r({
  name: "HeaderSeparator",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, anchor: { Width: 5, Height: 34 }, background: "Common/HeaderTabSeparator.png" });
  }
}), L = r({
  name: "PanelTitle",
  slots: Object,
  props: {
    alignment: {
      type: String,
      default: "Start"
    },
    text: {
      type: String,
      default: ""
    }
  },
  setup(e, { slots: o }) {
    const { alignment: n, text: a, ...l } = e;
    return () => t("Group", { ...l, layoutMode: "Top" }, [
      t("Label", { elStyle: { RenderBold: !0, VerticalAlignment: "Center", FontSize: 15, TextColor: "#afc2c3", HorizontalAlignment: e.alignment }, anchor: { Height: 35, Horizontal: 8 }, text: e.text, id: "PanelTitle" }),
      t("Group", { background: "#393426(0.5)", anchor: { Height: 1 } })
    ]);
  }
}), U = r({
  name: "VerticalSeparator",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, background: { TexturePath: "Common/ContainerVerticalSeparator.png" }, anchor: { Width: 6, Top: -2 } });
  }
}), G = r({
  name: "PanelSeparatorFancy",
  slots: Object,
  props: {
    anchor: {
      type: Object,
      default: () => ({})
    }
  },
  setup(e, { slots: o }) {
    const { anchor: n, ...a } = e;
    return () => t("Group", { ...a, layoutMode: "Left", anchor: e.anchor }, [
      t("Group", { flexWeight: 1, background: "Common/ContainerPanelSeparatorFancyLine.png" }),
      t("Group", { anchor: { Width: 11 }, background: "Common/ContainerPanelSeparatorFancyDecoration.png" }),
      t("Group", { flexWeight: 1, background: "Common/ContainerPanelSeparatorFancyLine.png" })
    ]);
  }
}), w = r({
  name: "Container",
  slots: Object,
  props: {
    contentPadding: {
      type: Object,
      default: () => ({ Full: 17 })
    },
    closeButton: {
      type: Boolean,
      default: !1
    }
  },
  setup(e, { slots: o }) {
    const { contentPadding: n, closeButton: a, ...l } = e;
    return () => t("Group", { ...l }, [
      t("Group", { anchor: { Height: 38, Top: 0 }, padding: { Top: 7 }, background: { TexturePath: "Common/ContainerHeaderNoRunes.png", HorizontalBorder: 35, VerticalBorder: 0 }, id: "Title" }, o.title ? o.title() : []),
      t("Group", { layoutMode: "Top", padding: e.contentPadding, anchor: { Top: 38 }, background: { TexturePath: "Common/ContainerPatch.png", Border: 23 }, id: "Content" }, o.content ? o.content() : []),
      t("Button", { anchor: { Width: 32, Height: 32, Top: -8, Right: -8 }, elStyle: { Default: { Background: "Common/ContainerCloseButton.png" }, Hovered: { Background: "Common/ContainerCloseButtonHovered.png" }, Pressed: { Background: "Common/ContainerCloseButtonPressed.png" }, Sounds: { Activate: { SoundPath: "Sounds/ButtonsCancelActivate.ogg", MinPitch: -0.4, MaxPitch: 0.4, Volume: 6 }, MouseHover: { SoundPath: "Sounds/ButtonsLightHover.ogg", Volume: 6 } } }, visible: e.closeButton, id: "CloseButton" }, o.closeButton ? o.closeButton() : [])
    ]);
  }
}), W = r({
  name: "DecoratedContainer",
  slots: Object,
  props: {
    contentPadding: {
      type: Object,
      default: () => ({ Full: 17 })
    },
    closeButton: {
      type: Boolean,
      default: !1
    }
  },
  setup(e, { slots: o }) {
    const { contentPadding: n, closeButton: a, ...l } = e;
    return () => t("Group", { ...l }, [
      t("Group", { anchor: { Height: 38, Top: 0 }, background: { TexturePath: "Common/ContainerHeader.png", HorizontalBorder: 50, VerticalBorder: 0 }, padding: { Top: 7 }, id: "Title" }, o.title ? o.title() : [t("Group", { anchor: { Width: 236, Height: 11, Top: -12 }, background: "Common/ContainerDecorationTop.png", id: "ContainerDecorationTop" })]),
      t("Group", { layoutMode: "Top", anchor: { Top: 38 }, padding: e.contentPadding, background: { TexturePath: "Common/ContainerPatch.png", Border: 23 }, id: "Content" }, o.content ? o.content() : []),
      t("Group", { anchor: { Width: 236, Height: 11, Bottom: -6 }, background: "Common/ContainerDecorationBottom.png", id: "ContainerDecorationBottom" }),
      t("Button", { anchor: { Width: 32, Height: 32, Top: -8, Right: -8 }, elStyle: { Default: { Background: "Common/ContainerCloseButton.png" }, Hovered: { Background: "Common/ContainerCloseButtonHovered.png" }, Pressed: { Background: "Common/ContainerCloseButtonPressed.png" }, Sounds: { Activate: { SoundPath: "Sounds/ButtonsCancelActivate.ogg", MinPitch: -0.4, MaxPitch: 0.4, Volume: 6 }, MouseHover: { SoundPath: "Sounds/ButtonsLightHover.ogg", Volume: 6 } } }, visible: e.closeButton, id: "CloseButton" }, o.closeButton ? o.closeButton() : [])
    ]);
  }
}), _ = r({
  name: "PageOverlay",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, background: "#000000(0.45)" });
  }
}), M = r({
  name: "BackButton",
  slots: Object,
  props: {},
  setup(e, { slots: o }) {
    const n = e;
    return () => t("Group", { ...n, layoutMode: "Left", anchor: { Left: 50, Bottom: 50, Width: 110, Height: 27 } }, [
      t("BackButton", {})
    ]);
  }
}), N = {
  Panel: u,
  TitleLabel: c,
  TextButton: s,
  Button: i,
  CancelTextButton: p,
  CancelButton: g,
  SmallSecondaryTextButton: m,
  SmallTertiaryTextButton: B,
  SecondaryTextButton: h,
  SecondaryButton: C,
  TertiaryTextButton: S,
  TertiaryButton: b,
  CloseButton: x,
  CheckBox: P,
  CheckBoxWithLabel: T,
  TextField: y,
  NumberField: f,
  DropdownBox: H,
  ContentSeparator: k,
  DefaultSpinner: v,
  ActionButtonContainer: z,
  ActionButtonSeparator: A,
  VerticalActionButtonSeparator: R,
  Subtitle: D,
  Title: O,
  HeaderSearch: j,
  HeaderTextButton: F,
  HeaderSeparator: V,
  PanelTitle: L,
  VerticalSeparator: U,
  PanelSeparatorFancy: G,
  Container: w,
  DecoratedContainer: W,
  PageOverlay: _,
  BackButton: M
};
export {
  N as Common
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiQ29tbW9uLmpzIiwic291cmNlcyI6W10sInNvdXJjZXNDb250ZW50IjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OzsifQ==

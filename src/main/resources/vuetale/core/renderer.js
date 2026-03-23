import { createRenderer as l } from "vue";
const g = (r) => l({
  createElement: (e) => (console.log(`createElement: ${e}`), ktBridge.createElement(r, e)),
  createText: (e) => (console.log(`createText: ${e}`), ktBridge.createText(r, e)),
  createComment: (e) => (console.log(`createComment: ${e}`), ktBridge.createComment(r, e)),
  setText: (e, t) => (console.log(`setText: ${t}`, JSON.stringify(e, null, 2)), ktBridge.setText(r, e, t)),
  setElementText: (e, t) => (console.log(`setElementText: ${t}`, JSON.stringify(e, null, 2)), ktBridge.setElementText(r, e, t)),
  parentNode: (e) => (console.log("parentNode", JSON.stringify(e, null, 2)), ktBridge.parentNode(r, e)),
  nextSibling: (e) => (console.log("nextSibling", JSON.stringify(e, null, 2)), ktBridge.nextSibling(r, e)),
  insert: (e, t) => {
    console.log("insert", JSON.stringify(e, null, 2), JSON.stringify(t, null, 2)), ktBridge.insert(r, e, t);
  },
  remove: (e) => {
    console.log("remove", JSON.stringify(e, null, 2)), ktBridge.remove(r, e);
  },
  patchProp: (e, t, n, o) => {
    console.log(`patchProp: ${t}`, n, o), console.log(`patchPropJson: ${t}`, JSON.stringify(n, null, 2), JSON.stringify(o, null, 2)), ktBridge.patchProp(r, e, t, n, o);
  }
});
export {
  g as hytaleRenderer
};

import { assertEquals } from "@std/assert";
import {
  extractTypeCandidates,
  parseElementPage,
  parseTypePage,
} from "./main.ts";

Deno.test("parseElementPage extracts properties and callbacks", () => {
  const html = `
    <html>
      <body>
        <h1>ActionButton</h1>
        <h2 id="properties">Properties</h2>
        <div>
          <table>
            <thead>
              <tr><th>Name</th><th>Type</th><th>Description</th></tr>
            </thead>
            <tbody>
              <tr><td><strong>Alignment</strong></td><td>ActionButtonAlignment</td><td>The alignment.</td></tr>
              <tr><td><strong>Anchor</strong></td><td><a href="../../property-types/anchor">Anchor</a></td><td>Positioning.</td></tr>
            </tbody>
          </table>
        </div>

        <h2 id="event-callbacks">Event Callbacks</h2>
        <div>
          <table>
            <thead>
              <tr><th>Name</th><th>Description</th></tr>
            </thead>
            <tbody>
              <tr><td><strong>Activating</strong></td><td></td></tr>
              <tr><td><strong>DoubleClicking</strong></td><td>When double clicked.</td></tr>
            </tbody>
          </table>
        </div>
      </body>
    </html>
  `;

  const parsed = parseElementPage(html);
  assertEquals(parsed.name, "ActionButton");
  assertEquals(parsed.properties.length, 2);
  assertEquals(parsed.properties[0].name, "Alignment");
  assertEquals(parsed.properties[0].type, "ActionButtonAlignment");
  assertEquals(parsed.properties[1].name, "Anchor");
  assertEquals(parsed.callbacks.length, 2);
  assertEquals(parsed.callbacks[0].name, "Activating");
  assertEquals(parsed.callbacks[1].description, "When double clicked.");
});

Deno.test("parseTypePage parses object-like type table", () => {
  const html = `
    <html>
      <body>
        <h1>Anchor</h1>
        <table>
          <thead>
            <tr><th>Name</th><th>Type</th><th>Description</th></tr>
          </thead>
          <tbody>
            <tr><td><strong>Left</strong></td><td>Integer</td><td>Left position.</td></tr>
            <tr><td><strong>Top</strong></td><td>Integer</td><td>Top position.</td></tr>
          </tbody>
        </table>
      </body>
    </html>
  `;

  const parsed = parseTypePage(html);
  if (parsed.kind !== "object") {
    throw new Error("Expected object type");
  }
  assertEquals(parsed.name, "Anchor");
  assertEquals(parsed.fields.Left, "Integer");
  assertEquals(parsed.fields.Top, "Integer");
});

Deno.test("parseTypePage parses enum-like type table", () => {
  const html = `
    <html>
      <body>
        <h1>ActionButtonAlignment</h1>
        <table>
          <thead>
            <tr><th>Name</th><th>Description</th></tr>
          </thead>
          <tbody>
            <tr><td><strong>Left</strong></td><td></td></tr>
            <tr><td><strong>Right</strong></td><td></td></tr>
          </tbody>
        </table>
      </body>
    </html>
  `;

  const parsed = parseTypePage(html);
  if (parsed.kind !== "enum") {
    throw new Error("Expected enum type");
  }
  assertEquals(parsed.name, "ActionButtonAlignment");
  assertEquals(parsed.values, ["Left", "Right"]);
});

Deno.test("extractTypeCandidates handles unions and generics", () => {
  const fromUnion = extractTypeCandidates("PatchStyle / String");
  assertEquals(fromUnion, ["PatchStyle"]);

  const fromGeneric = extractTypeCandidates("List<LabelSpan>");
  assertEquals(fromGeneric, ["LabelSpan"]);

  const fromDictionary = extractTypeCandidates("Dictionary`2");
  assertEquals(fromDictionary, []);
});

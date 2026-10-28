import { afterEach, describe, expect, it, vi } from "vitest";
import { cleanup, fireEvent, render, screen } from "@testing-library/react";
import { GreetingForm } from "./greeting-form";

const get = vi.fn();
vi.mock("@/lib/api/client", () => ({ api: { GET: (...args: unknown[]) => get(...args) } }));

afterEach(() => {
  cleanup();
  get.mockReset();
});

function submit(name: string) {
  render(<GreetingForm />);
  fireEvent.change(screen.getByLabelText("Name"), { target: { value: name } });
  fireEvent.click(screen.getByRole("button", { name: "Greet" }));
}

describe("GreetingForm", () => {
  it("shows the greeting when the API succeeds", async () => {
    get.mockResolvedValue({ data: { message: "Hello, Ada!" } });

    submit("Ada");

    expect((await screen.findByRole("status")).textContent).toBe("Hello, Ada!");
    expect(get).toHaveBeenCalledWith("/v1/greetings/{name}", { params: { path: { name: "Ada" } } });
  });

  it("shows the problem detail when the API fails", async () => {
    get.mockResolvedValue({ error: { detail: "No greeting for name 'Nobody'" } });

    submit("Nobody");

    expect((await screen.findByRole("alert")).textContent).toBe("No greeting for name 'Nobody'");
  });
});

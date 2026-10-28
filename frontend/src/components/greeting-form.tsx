"use client";

import { useState, type FormEvent } from "react";
import { api } from "@/lib/api/client";

export function GreetingForm() {
  const [name, setName] = useState("");
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMessage(null);
    setError(null);
    const { data, error: problem } = await api.GET("/v1/greetings/{name}", {
      params: { path: { name } },
    });
    if (data) {
      setMessage(data.message);
    } else {
      setError(problem?.detail ?? "Something went wrong");
    }
  }

  return (
    <form onSubmit={onSubmit}>
      <label htmlFor="name">Name</label>
      <input id="name" value={name} onChange={(e) => setName(e.target.value)} />
      <button type="submit">Greet</button>
      {message && <p role="status">{message}</p>}
      {error && <p role="alert">{error}</p>}
    </form>
  );
}

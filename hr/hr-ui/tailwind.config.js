/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#4F46E5",
        "slate-900": "#0f172a",
        "slate-800": "#1e293b",
        "slate-50": "#f8fafc",
      },
      borderRadius: {
        'xl': '12px'
      }
    },
  },
  plugins: [],
}

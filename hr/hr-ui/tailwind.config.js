module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#4F46E5", // 品牌主色 (科技蓝紫)
        "slate-900": "#0f172a", // 深色导航背景
        "slate-800": "#1e293b",
      },
      borderRadius: {
        'xl': '12px'
      }
    },
  },
  plugins: [],
}

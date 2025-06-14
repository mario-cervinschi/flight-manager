/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}"
  ],
  theme: {
    extend: {
      colors: {
        'custom-nav-via': '#064e3b',
        'custom-nav-secondary': '#022c22',
        'custom-nav-primary': '#134e4a',
        'custom-nav-selected': '#1e2d29',
        'custom-background': '#f0fdf4',
        'custom-secondary-background': '#dcfce5',
        'custom-card-background': '#ffffff',
        'custom-hover-link': '#064e3b',
      },
    },
  },
  plugins: [],
}


module.exports = {
  testEnvironment: "node",
  testPathIgnorePatterns: ["/lib/", "/node_modules/", "ignore.*", "/build/"],
  transform: {
    "\\.ts$": "esbuild-runner/jest",
  },
}

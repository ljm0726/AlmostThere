<template>
  <div id="roulette">
    <canvas ref="canvas" :width="size" :height="size"></canvas>
    <button @click="rotate" :disabled="isRotating">돌리기</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      options: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      size: 300,
      colors: [
        "#dc0936",
        "#e6471d",
        "#f7a416",
        "#efe61f ",
        "#60b236",
        "#209b6c",
        "#169ed8",
        "#3f297e",
        "#87207b",
        "#be107f",
        "#e7167b",
      ],
      angle: 0,
      rotation: 0,
      target: null,
      isRotating: false,
      radius: 0,
    };
  },
  mounted() {
    this.radius = this.$refs.canvas.width / 2;
    this.drawRoulette();
  },
  methods: {
    drawArrow() {
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext("2d");
      const radius = this.radius;
      const arrowHeight = 30;
      const arrowWidth = 20;

      ctx.save();
      ctx.translate(radius, radius);
      ctx.rotate(Math.PI / 2); // 시계 방향으로 90도 회전

      ctx.beginPath();
      ctx.moveTo(0, 0);
      ctx.lineTo(-arrowWidth / 2, -arrowHeight);
      ctx.lineTo(arrowWidth / 2, -arrowHeight);
      ctx.closePath();
      ctx.fillStyle = "black";
      ctx.fill();

      ctx.restore();
    },
    drawRoulette() {
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext("2d");
      const radius = this.radius;

      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // 삼각형 모양의 화살표 그리기
      this.drawArrow();

      this.options.forEach((option, index) => {
        const angle = (index * Math.PI * 2) / this.options.length + this.angle;
        const color = this.colors[index % this.colors.length];

        ctx.beginPath();
        ctx.fillStyle = color;
        ctx.arc(
          radius,
          radius,
          radius * 0.8,
          angle,
          angle + (Math.PI * 2) / this.options.length
        );
        ctx.lineTo(radius, radius);
        ctx.fill();

        ctx.save();
        ctx.translate(radius, radius);
        ctx.rotate(angle + Math.PI / this.options.length);
        ctx.fillStyle = "#dc0936";
        ctx.font = `${radius / 10}px Arial`;
        ctx.fillText(option, radius * 0.6, 0);
        ctx.restore();
      });
    },
    rotate() {
      if (this.isRotating) {
        return;
      }

      this.isRotating = true;

      const canvas = this.$refs.canvas;
      const radius = this.radius;
      canvas;
      radius;
      const targetIndex = Math.floor(Math.random() * this.options.length);
      const targetAngle = (targetIndex * Math.PI * 2) / this.options.length;

      const rotateInterval = setInterval(() => {
        this.rotation += (Math.PI / 180) * 5;
        this.angle += (Math.PI / 180) * 5;

        this.drawRoulette();

        if (this.rotation >= Math.PI * 2 + targetAngle) {
          clearInterval(rotateInterval);

          this.angle =
            targetAngle -
            (this.rotation % ((Math.PI * 2) / this.options.length));
          this.rotation = 0;
          this.isRotating = false;

          this.target = this.options[targetIndex];
          alert(`선택된 항목: ${this.target}`);
        }
      }, 10);
    },
  },
};
</script>

<style scoped>
#roulette {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

canvas {
  border: 2px solid #000;
  border-radius: 50%;
  margin-bottom: 10px;
}

button {
  padding: 10px;
  font-size: 18px;
  font-weight: bold;
  background-color: #209b6c;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>

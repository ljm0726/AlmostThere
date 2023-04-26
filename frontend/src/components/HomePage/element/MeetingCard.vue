<template>
  <v-card
    width="325px"
    height="176px"
    elevation="0"
    style="
      border: 1px solid #092a49;
      border-radius: 15px;
      box-shadow: 0px 5px 20px -10px #092a49;
    "
  >
    <div
      style="
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        align-items: flex-end;
      "
    >
      <div
        style="
          background: #092a49;
          border-radius: 0px 13px;
          width: 150px;
          height: 30px;
          font-size: 10px;
        "
      >
        <span class="mt-2 d-flex justify-center align-center white-font"
          >약 {{ meeting.meetingTime | remainTime }} 후 예정</span
        >
      </div>
    </div>
    <v-sheet width="91%" class="white-col-1 ml-5" color="transparent">
      <div>
        <span class="lg-font bold-font main-col-1">{{
          meeting.meetingName
        }}</span>
        <v-divider class="mt-2 border-opacity-25" style="width: 5"></v-divider>
      </div>
      <div class="mt-2">
        <div>
          <span class="xxxs-font medium-font main-col-1">장소</span>
          <span class="ml-1 xxxs-font light-font main-col-1">{{
            meeting.meetingPlace
          }}</span>
        </div>
        <div>
          <span class="xxxs-font medium-font main-col-1">일시</span>
          <span class="ml-1 xxxs-font light-font main-col-1">{{
            meeting.meetingTime | formatDate
          }}</span>
        </div>
      </div>
      <div class="mt-2 d-flex flex-row justify-space-between">
        <section class="avatars-group stacked">
          <div
            v-for="avatar in avatarsStackedLimited"
            :key="`avatar-id-${avatar.id}`"
            class="avatars-group__item"
          >
            <v-avatar size="27px">
              <v-img
                src="https://cdn.vuetifyjs.com/images/john.jpg"
                alt="John"
              ></v-img>
            </v-avatar>
          </div>
          <div v-if="this.meeting.meetingMembers.length > 6">
            <div class="avatars-group__item more">
              <v-avatar color="white" size="27px">
                <v-menu
                  v-model="stackedMenu"
                  lazy
                  left
                  :max-height="menuMaxHeight"
                  nudge-bottom="8"
                  offset-y
                >
                </v-menu>
                <span class="xxxs-font light-font main-col-1"
                  >+{{ meeting.meetingMembers.length - 6 }}</span
                >
              </v-avatar>
            </div>
          </div>
        </section>
        <div class="d-flex flex-column justify-flex-end align-flex-end">
          <v-btn
            class="d-flex justify-center align-center"
            id="square-btn"
            outlined
            color="var(--main-col-1)"
            rounded
            small
          >
            <v-icon>$vuetify.icons.map_outline</v-icon>
          </v-btn>
        </div>
      </div>
    </v-sheet>
  </v-card>
</template>

<script>
import moment from "moment";

export default {
  name: "MeetingCard",
  props: {
    meeting: Object,
  },
  data() {
    return {
      menuMaxHeight: `${60 * 5 + 4}px`,
      stackedLimit: 6,
      stackedMenu: false,
    };
  },
  filters: {
    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      const month = date.getMonth();
      const day = date.getDate();
      const hour = date.getHours();
      const min = date.getMinutes();
      const result = `${year}년 ${
        month + 1 < 10 ? `0${month + 1}` : month + 1
      }월 ${day < 10 ? `0${day}` : day}일
    ${
      hour < 13
        ? ` 오전 ${hour - 12 < 10 ? `0${hour - 12}` : hour - 12}`
        : ` 오후 ${hour - 12 < 10 ? `0${hour - 12}` : hour - 12}`
    }시 ${min < 10 ? `0${min}` : min}분`;
      return result;
    },
    remainTime(value) {
      const now = moment();
      const time = moment(value, "YYYY-MM-DD hh:mm:ss");
      const hour = moment.duration(time.diff(now)).hours();
      const min = moment.duration(time.diff(now)).minutes();
      const remain = hour == 0 ? `${min}분` : `${hour}시간 ${min}분`;
      return remain;
    },
  },
  computed: {
    avatarsStackedLimited() {
      return this.meeting.meetingMembers &&
        this.meeting.meetingMembers.length > 0
        ? this.meeting.meetingMembers.slice(0, this.stackedLimit)
        : null;
    },
  },
};
</script>

<style lang="stylus" scoped>
$avatar-size = 40px
$base-unit = 5px
$border-width = $base-unit / 4

.avatars-group
  &.grid
    display grid
    grid-gap $base-unit
    grid-template-columns repeat(auto-fit, minmax(($avatar-size + $base-unit), 1fr))
  &.stacked
    display flex
    flex-direction row
    direction ltr
    max-width 100%
    overflow hidden
    white-space nowrap
    > *
      margin-right -($base-unit)
      &:last-of-type
        padding-right ($base-unit * 2)
  &__item
    cursor default
    transition all .1s ease-out
    &.more
      align-items center
      display flex
      &:hover
        transform none
    &:hover
      transform translateY(-($base-unit / 2))
      z-index 1
  .v-avatar
    border: 2px solid #092a49
    img
      padding $border-width
    span
      align-items center
      display flex
      height 100%
      justify-content center
      letter-spacing 0.1rem
      width inherit

.v-avatar.bordered
  box-shadow 0px 0px 0px $border-width #fff inset
  img
    padding $border-width
.v-avatar.bordered.small
    box-shadow 0px 0px 0px ($border-width - 1) #fff inset
    img
      padding ($border-width - 1)

.presence
  box-shadow 0px 0px 0px ($border-width) #fff inset
  border-radius 50%
  bottom 0px
  display block
  height ($base-unit * 1.75)
  position absolute
  width ($base-unit * 1.75)
.v-avatar.bordered.small
  .presence
    box-shadow 0px 0px 0px ($border-width - 1) #fff inset
    display block
    height ($base-unit * 1)
    position absolute
    width ($base-unit * 1)
</style>
